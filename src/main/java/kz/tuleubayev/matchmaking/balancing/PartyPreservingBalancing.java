package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Friends who queued together stay together: every premade party goes to one team as a block.
 * Blocks are placed biggest first into the weaker team that still has room.
 */
public class PartyPreservingBalancing implements TeamBalancingStrategy {

    @Override
    public Teams split(List<Player> lobby) {
        int cap = lobby.size() / 2;
        List<Player> blue = new ArrayList<>();
        List<Player> red = new ArrayList<>();
        for (List<Player> group : groupsBiggestFirst(lobby)) {
            boolean blueFits = blue.size() + group.size() <= cap;
            boolean redFits = red.size() + group.size() <= cap;
            if (!blueFits && !redFits) {
                throw new IllegalArgumentException("Cannot keep party of " + group.size()
                        + " together in teams of " + cap);
            }
            if (blueFits && (!redFits || RatingMath.sum(blue) <= RatingMath.sum(red))) {
                blue.addAll(group);
            } else {
                red.addAll(group);
            }
        }
        return new Teams(blue, red);
    }

    private static List<List<Player>> groupsBiggestFirst(List<Player> lobby) {
        Map<String, List<Player>> groups = new LinkedHashMap<>();
        for (Player p : lobby) {
            String key = p.isSolo() ? "solo:" + p.nickname() : "party:" + p.partyId();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
        }
        List<List<Player>> ordered = new ArrayList<>(groups.values());
        ordered.sort(Comparator.<List<Player>>comparingInt(List::size).reversed()
                .thenComparing(Comparator.<List<Player>>comparingInt(RatingMath::sum).reversed()));
        return ordered;
    }

    @Override
    public String name() {
        return "Party preserving";
    }
}
