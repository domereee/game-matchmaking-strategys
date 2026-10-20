package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.GameMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** One test per selector rule. */
class TeamBalancingSelectorTest {

    private final TeamBalancingSelector selector = new TeamBalancingSelector(42);

    @Test
    void customGamesAreRandomEvenWithParties() {
        assertInstanceOf(RandomBalancing.class, selector.choose(GameMode.CUSTOM, Lobbies.TWO_DUOS));
    }

    @Test
    void rankedGamesUseGreedyEvenWithParties() {
        assertInstanceOf(GreedyBalancing.class, selector.choose(GameMode.RANKED, Lobbies.TWO_DUOS));
    }

    @Test
    void casualGamesWithAPremadePartyKeepFriendsTogether() {
        assertInstanceOf(PartyPreservingBalancing.class, selector.choose(GameMode.CASUAL, Lobbies.TWO_DUOS));
    }

    @Test
    void casualGamesWithOnlySoloPlayersUseSnakeDraft() {
        assertInstanceOf(SnakeDraftBalancing.class, selector.choose(GameMode.CASUAL, Lobbies.EIGHT_SOLO));
    }

    @Test
    void modeIsRequired() {
        assertThrows(NullPointerException.class, () -> selector.choose(null, Lobbies.EIGHT_SOLO));
    }
}
