package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.List;

/**
 * Strategy family 1: one way of splitting a valid lobby (even size, unique players) into two
 * equal teams.
 */
public interface TeamBalancingStrategy {

    Teams split(List<Player> lobby);

    /** Short human-readable name for logs and the demo. */
    String name();
}
