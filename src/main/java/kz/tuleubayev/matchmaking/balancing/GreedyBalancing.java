package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes players from strongest to weakest and always gives the next one to the team with the
 * lower total rating (while it still has room). Usually gives the smallest rating gap.
 */
public class GreedyBalancing implements TeamBalancingStrategy {

    @Override
    public Teams split(List<Player> lobby) {
        int cap = lobby.size() / 2;
        List<Player> blue = new ArrayList<>();
        List<Player> red = new ArrayList<>();
        for (Player p : RatingMath.byRatingDesc(lobby)) {
            if (blue.size() == cap) {
                red.add(p);
            } else if (red.size() == cap) {
                blue.add(p);
            } else if (RatingMath.sum(blue) <= RatingMath.sum(red)) {
                blue.add(p);
            } else {
                red.add(p);
            }
        }
        return new Teams(blue, red);
    }

    @Override
    public String name() {
        return "Greedy";
    }
}
