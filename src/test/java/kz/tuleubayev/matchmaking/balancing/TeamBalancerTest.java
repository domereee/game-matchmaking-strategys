package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behaviour of TeamBalancer through its public API only. This file is committed BEFORE the
 * Strategy refactoring and must pass unchanged AFTER it.
 */
class TeamBalancerTest {

    // one star player (A), one weak player (F), four average ones — no parties
    static final List<Player> SIX_SOLO = List.of(
            Player.solo("A", 2400), Player.solo("B", 1800), Player.solo("C", 1700),
            Player.solo("D", 1600), Player.solo("E", 1500), Player.solo("F", 1000));

    static final List<Player> WITH_PARTY = List.of(
            new Player("Damir", 2000, "P1"), new Player("Aruzhan", 1900, "P1"),
            Player.solo("Timur", 1800), Player.solo("Dana", 1700));

    private static List<String> names(List<Player> team) {
        return team.stream().map(Player::nickname).toList();
    }

    @Test
    void snakeDraftPicksAbbaByRating() {
        Teams t = new TeamBalancer(BalancingMethod.SNAKE_DRAFT).balance(SIX_SOLO);
        assertEquals(List.of("A", "D", "E"), names(t.blue()));
        assertEquals(List.of("B", "C", "F"), names(t.red()));
        assertEquals(1000, t.ratingGap());
    }

    @Test
    void greedyPutsEachPlayerIntoTheWeakerTeam() {
        Teams t = new TeamBalancer(BalancingMethod.GREEDY).balance(SIX_SOLO);
        assertEquals(List.of("A", "D", "F"), names(t.blue()));
        assertEquals(List.of("B", "C", "E"), names(t.red()));
        assertEquals(0, t.ratingGap());
    }

    @Test
    void partyPreservingKeepsAPremadePartyOnOneTeam() {
        Teams t = new TeamBalancer(BalancingMethod.PARTY_PRESERVING).balance(WITH_PARTY);
        assertEquals(List.of("Damir", "Aruzhan"), names(t.blue()));
        assertEquals(List.of("Timur", "Dana"), names(t.red()));
    }

    @Test
    void partyPreservingRefusesAPartyBiggerThanATeam() {
        List<Player> bigParty = List.of(new Player("A", 1, "P"), new Player("B", 1, "P"),
                new Player("C", 1, "P"), Player.solo("D", 1));
        assertThrows(IllegalArgumentException.class,
                () -> new TeamBalancer(BalancingMethod.PARTY_PRESERVING).balance(bigParty));
    }

    @Test
    void randomIsRepeatableWithTheFixedSeedAndKeepsTeamSizesEqual() {
        Teams first = new TeamBalancer(BalancingMethod.RANDOM).balance(SIX_SOLO);
        Teams second = new TeamBalancer(BalancingMethod.RANDOM).balance(SIX_SOLO);
        assertEquals(first, second);
        assertEquals(3, first.blue().size());
        assertEquals(3, first.red().size());
    }

    @Test
    void methodCanBeSwitchedAtRuntime() {
        TeamBalancer balancer = new TeamBalancer(BalancingMethod.SNAKE_DRAFT);
        Teams snake = balancer.balance(SIX_SOLO);
        balancer.setMethod(BalancingMethod.GREEDY);
        Teams greedy = balancer.balance(SIX_SOLO);
        assertTrue(!snake.equals(greedy));
        assertTrue(balancer.strategyName().toUpperCase().contains("GREEDY"));
    }

    @Test
    void invalidLobbiesAreRejected() {
        TeamBalancer balancer = new TeamBalancer(BalancingMethod.GREEDY);
        assertThrows(IllegalArgumentException.class, () -> balancer.balance(List.of(Player.solo("A", 1))));
        assertThrows(IllegalArgumentException.class, () -> balancer.balance(SIX_SOLO.subList(0, 5)));
        assertThrows(IllegalArgumentException.class,
                () -> balancer.balance(List.of(Player.solo("A", 1), Player.solo("a", 2))));
        assertThrows(IllegalArgumentException.class, () -> balancer.balance(null));
    }
}
