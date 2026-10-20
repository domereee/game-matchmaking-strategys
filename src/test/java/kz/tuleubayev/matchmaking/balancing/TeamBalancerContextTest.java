package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** The context only delegates: any strategy, even one written later, plugs in unchanged. */
class TeamBalancerContextTest {

    /** A strategy the context has never heard of: everyone sorted by nickname, cut in half. */
    static final class AlphabeticalBalancing implements TeamBalancingStrategy {
        @Override
        public Teams split(List<Player> lobby) {
            List<Player> sorted = lobby.stream().sorted((a, b) -> a.nickname().compareTo(b.nickname())).toList();
            return new Teams(sorted.subList(0, sorted.size() / 2), sorted.subList(sorted.size() / 2, sorted.size()));
        }

        @Override
        public String name() {
            return "Alphabetical";
        }
    }

    @Test
    void aNewStrategyWorksWithoutChangingTheContext() {
        TeamBalancer balancer = new TeamBalancer(new AlphabeticalBalancing());
        Teams t = balancer.balance(Lobbies.FOUR_SOLO);
        assertEquals(List.of("A", "B"), Lobbies.names(t.blue()));
        assertEquals("Alphabetical", balancer.strategyName());
    }

    @Test
    void setStrategySwapsTheAlgorithmAtRuntime() {
        TeamBalancer balancer = new TeamBalancer(new SnakeDraftBalancing());
        balancer.setStrategy(new AlphabeticalBalancing());
        assertEquals("Alphabetical", balancer.strategyName());
        balancer.setMethod(BalancingMethod.PARTY_PRESERVING);
        assertEquals("Party preserving", balancer.strategyName());
    }

    @Test
    void theContextReturnsExactlyWhatTheStrategyReturns() {
        Teams fixed = new Teams(List.of(Lobbies.FOUR_SOLO.get(0)), List.of(Lobbies.FOUR_SOLO.get(1)));
        TeamBalancer balancer = new TeamBalancer(new TeamBalancingStrategy() {
            @Override
            public Teams split(List<Player> lobby) {
                return fixed;
            }

            @Override
            public String name() {
                return "Fixed";
            }
        });
        assertSame(fixed, balancer.balance(Lobbies.FOUR_SOLO));
    }

    @Test
    void nullStrategyIsRejected() {
        assertThrows(NullPointerException.class, () -> new TeamBalancer((TeamBalancingStrategy) null));
        assertThrows(NullPointerException.class, () -> new TeamBalancer(new GreedyBalancing()).setStrategy(null));
    }
}
