package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.GameMode;
import kz.tuleubayev.matchmaking.model.Player;

import java.util.List;
import java.util.Objects;

/**
 * Chooses a balancing strategy automatically from the current situation. Choosing is this
 * class's only job, so this is the one place where if/else on the situation is allowed.
 * <pre>
 *   CUSTOM game                      -> Random            (fun, ratings don't matter)
 *   RANKED game                      -> Greedy            (smallest rating gap, parties may split)
 *   CASUAL with a premade party      -> Party preserving  (friends stay together)
 *   CASUAL, everyone solo            -> Snake draft       (simple and fair enough)
 * </pre>
 */
public class TeamBalancingSelector {

    private final TeamBalancingStrategy random;
    private final TeamBalancingStrategy greedy = new GreedyBalancing();
    private final TeamBalancingStrategy partyPreserving = new PartyPreservingBalancing();
    private final TeamBalancingStrategy snakeDraft = new SnakeDraftBalancing();

    public TeamBalancingSelector(long randomSeed) {
        this.random = new RandomBalancing(randomSeed);
    }

    public TeamBalancingStrategy choose(GameMode mode, List<Player> lobby) {
        Objects.requireNonNull(mode, "mode");
        if (mode == GameMode.CUSTOM) {
            return random;
        }
        if (mode == GameMode.RANKED) {
            return greedy;
        }
        if (hasPremadeParty(lobby)) {
            return partyPreserving;
        }
        return snakeDraft;
    }

    private static boolean hasPremadeParty(List<Player> lobby) {
        return lobby.stream().anyMatch(p -> !p.isSolo());
    }
}
