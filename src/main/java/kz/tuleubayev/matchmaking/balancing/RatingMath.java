package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Small helpers shared by the balancing strategies. */
final class RatingMath {

    private RatingMath() {
    }

    /** Strongest first; equal ratings ordered by nickname so results are repeatable. */
    static List<Player> byRatingDesc(List<Player> players) {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(Comparator.comparingInt(Player::rating).reversed().thenComparing(Player::nickname));
        return sorted;
    }

    static int sum(List<Player> players) {
        return players.stream().mapToInt(Player::rating).sum();
    }
}
