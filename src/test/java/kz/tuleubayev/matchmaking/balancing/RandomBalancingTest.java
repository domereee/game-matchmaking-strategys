package kz.tuleubayev.matchmaking.balancing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RandomBalancingTest {

    @Test
    void sameSeedGivesTheSameTeams() {
        assertEquals(new RandomBalancing(7).split(Lobbies.EIGHT_SOLO), new RandomBalancing(7).split(Lobbies.EIGHT_SOLO));
    }

    @Test
    void differentSeedsGiveDifferentTeams() {
        assertNotEquals(new RandomBalancing(1).split(Lobbies.EIGHT_SOLO),
                new RandomBalancing(2).split(Lobbies.EIGHT_SOLO));
    }

    @Test
    void doesNotSortByRating() {
        assertNotEquals(new SnakeDraftBalancing().split(Lobbies.EIGHT_SOLO),
                new RandomBalancing(1).split(Lobbies.EIGHT_SOLO));
        assertEquals("Random", new RandomBalancing(1).name());
    }
}
