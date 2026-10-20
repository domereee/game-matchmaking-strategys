package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.ArrayList;
import java.util.List;

/**
 * Like captains picking in a schoolyard: strongest players are drafted in the order
 * BLUE, RED, RED, BLUE, BLUE, RED, RED, BLUE... Fast and needs no maths.
 */
public class SnakeDraftBalancing implements TeamBalancingStrategy {

    @Override
    public Teams split(List<Player> lobby) {
        List<Player> sorted = RatingMath.byRatingDesc(lobby);
        List<Player> blue = new ArrayList<>();
        List<Player> red = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            int pos = i % 4;
            (pos == 0 || pos == 3 ? blue : red).add(sorted.get(i));
        }
        return new Teams(blue, red);
    }

    @Override
    public String name() {
        return "Snake draft";
    }
}
