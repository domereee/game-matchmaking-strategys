package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.LobbyRules;
import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.List;
import java.util.Objects;

/**
 * Context of strategy family 1. It holds one {@link TeamBalancingStrategy}, lets you swap it at
 * runtime and simply delegates the work to it. It never looks at which strategy it holds.
 */
public class TeamBalancer {

    public static final long RANDOM_SEED = 42L;

    private TeamBalancingStrategy strategy;

    public TeamBalancer(TeamBalancingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    /** Convenience for the built-in strategies. */
    public TeamBalancer(BalancingMethod method) {
        this(method.strategy());
    }

    public void setStrategy(TeamBalancingStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public void setMethod(BalancingMethod method) {
        setStrategy(method.strategy());
    }

    public String strategyName() {
        return strategy.name();
    }

    public Teams balance(List<Player> lobby) {
        LobbyRules.requireValid(lobby);
        return strategy.split(lobby);
    }
}
