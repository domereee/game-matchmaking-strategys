package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kz.tuleubayev.matchmaking.balancing.Lobbies.names;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SnakeDraftBalancingTest {

    private final SnakeDraftBalancing snake = new SnakeDraftBalancing();

    @Test
    void picksInAbbaOrderByRating() {
        Teams t = snake.split(Lobbies.EIGHT_SOLO);
        assertEquals(List.of("P1", "P4", "P5", "P8"), names(t.blue()));
        assertEquals(List.of("P2", "P3", "P6", "P7"), names(t.red()));
    }

    @Test
    void inputOrderDoesNotMatterOnlyRating() {
        List<Player> reversed = List.of(Lobbies.FOUR_SOLO.get(3), Lobbies.FOUR_SOLO.get(2),
                Lobbies.FOUR_SOLO.get(1), Lobbies.FOUR_SOLO.get(0));
        assertEquals(snake.split(Lobbies.FOUR_SOLO), snake.split(reversed));
        assertEquals(List.of("A", "D"), names(snake.split(reversed).blue()));
    }

    @Test
    void ignoresParties() {
        Teams t = snake.split(Lobbies.TWO_DUOS);
        assertEquals(false, Lobbies.sameTeam(t, "Damir", "Aruzhan"));
        assertEquals("Snake draft", snake.name());
    }
}
