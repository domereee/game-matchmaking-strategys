package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Ignores ratings completely: shuffles the lobby and cuts it in half. Used for fun custom
 * games. The seed is injected so results can be repeated in tests.
 */
public class RandomBalancing implements TeamBalancingStrategy {

    private final long seed;

    public RandomBalancing(long seed) {
        this.seed = seed;
    }

    @Override
    public Teams split(List<Player> lobby) {
        List<Player> shuffled = new ArrayList<>(lobby);
        Collections.shuffle(shuffled, new Random(seed));
        int cap = lobby.size() / 2;
        return new Teams(shuffled.subList(0, cap), shuffled.subList(cap, shuffled.size()));
    }

    @Override
    public String name() {
        return "Random";
    }
}
