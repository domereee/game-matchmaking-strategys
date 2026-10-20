package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyPreservingBalancingTest {

    private final PartyPreservingBalancing preserving = new PartyPreservingBalancing();

    @Test
    void everyPremadePartyStaysOnOneTeam() {
        Teams t = preserving.split(Lobbies.TWO_DUOS);
        assertTrue(Lobbies.sameTeam(t, "Damir", "Aruzhan"));
        assertTrue(Lobbies.sameTeam(t, "Timur", "Dana"));
    }

    @Test
    void theTwoStrongestDuosAreSentToOppositeTeams() {
        Teams t = preserving.split(Lobbies.TWO_DUOS);
        assertFalse(Lobbies.sameTeam(t, "Damir", "Timur"));
        assertEquals(4, t.blue().size());
    }

    @Test
    void aPartyBiggerThanHalfTheLobbyCannotBeKeptTogether() {
        List<Player> trio = List.of(new Player("A", 1, "P"), new Player("B", 1, "P"), new Player("C", 1, "P"),
                Player.solo("D", 1));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> preserving.split(trio));
        assertTrue(e.getMessage().contains("party of 3"));
    }

    @Test
    void withoutPartiesItStillBalancesByRating() {
        assertEquals(0, preserving.split(Lobbies.FOUR_SOLO).ratingGap());
        assertEquals("Party preserving", preserving.name());
    }
}
