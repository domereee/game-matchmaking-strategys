package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** The same contract checked on every strategy of family 1. */
class AllBalancingStrategiesTest {

    static Stream<TeamBalancingStrategy> strategies() {
        return Stream.of(new SnakeDraftBalancing(), new GreedyBalancing(), new PartyPreservingBalancing(),
                new RandomBalancing(42));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("strategies")
    void everyPlayerEndsUpInExactlyOneTeamAndTeamsHaveEqualSize(TeamBalancingStrategy strategy) {
        Teams t = strategy.split(Lobbies.TWO_DUOS);

        assertEquals(t.blue().size(), t.red().size(), strategy.name() + ": team sizes");
        List<Player> everyone = new ArrayList<>(t.blue());
        everyone.addAll(t.red());
        assertEquals(new HashSet<>(Lobbies.TWO_DUOS), new HashSet<>(everyone), strategy.name() + ": same players");
        assertEquals(Lobbies.TWO_DUOS.size(), everyone.size(), strategy.name() + ": nobody twice");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("strategies")
    void ratingGapIsNeverNegativeAndTotalRatingIsKept(TeamBalancingStrategy strategy) {
        Teams t = strategy.split(Lobbies.EIGHT_SOLO);
        int total = Lobbies.EIGHT_SOLO.stream().mapToInt(Player::rating).sum();
        assertEquals(total, t.blueRating() + t.redRating());
        assertEquals(true, t.ratingGap() >= 0);
    }
}
