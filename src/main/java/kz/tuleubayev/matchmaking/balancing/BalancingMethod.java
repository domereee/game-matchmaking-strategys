package kz.tuleubayev.matchmaking.balancing;

import java.util.function.Supplier;

/**
 * Named shortcuts to the built-in strategies, kept so code written before the refactoring
 * (and its tests) still works. New strategies do NOT need a constant here: pass them to
 * {@link TeamBalancer} directly.
 */
public enum BalancingMethod {
    SNAKE_DRAFT(SnakeDraftBalancing::new),
    GREEDY(GreedyBalancing::new),
    PARTY_PRESERVING(PartyPreservingBalancing::new),
    RANDOM(() -> new RandomBalancing(TeamBalancer.RANDOM_SEED));

    private final Supplier<TeamBalancingStrategy> factory;

    BalancingMethod(Supplier<TeamBalancingStrategy> factory) {
        this.factory = factory;
    }

    public TeamBalancingStrategy strategy() {
        return factory.get();
    }
}
