package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kz.tuleubayev.matchmaking.balancing.Lobbies.names;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GreedyBalancingTest {

    private final GreedyBalancing greedy = new GreedyBalancing();

    @Test
    void alwaysFeedsTheWeakerTeam() {
        Teams t = greedy.split(Lobbies.FOUR_SOLO);
        // A(2000) blue, B(1800) red, C(1600) red (blue is stronger), D(1400) blue (red is full)
        assertEquals(List.of("A", "D"), names(t.blue()));
        assertEquals(List.of("B", "C"), names(t.red()));
        assertEquals(0, t.ratingGap());
    }

    @Test
    void beatsSnakeDraftWhenOneStarPlayerDominates() {
        List<Player> star = List.of(Player.solo("Star", 3000), Player.solo("B", 1500), Player.solo("C", 1500),
                Player.solo("D", 1500), Player.solo("E", 1500), Player.solo("F", 1500));
        assertTrue(greedy.split(star).ratingGap() <= new SnakeDraftBalancing().split(star).ratingGap());
    }

    @Test
    void neverOverfillsATeam() {
        Teams t = greedy.split(Lobbies.EIGHT_SOLO);
        assertEquals(4, t.blue().size());
        assertEquals(4, t.red().size());
        assertEquals("Greedy", greedy.name());
    }
}
