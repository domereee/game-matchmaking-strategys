package kz.tuleubayev.matchmaking.server;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Picks the server with the smallest share of used slots. Spreads players evenly so no machine
 * overheats during peak hours.
 */
public class LeastLoadedSelection implements ServerSelectionStrategy {

    @Override
    public Optional<GameServer> choose(List<GameServer> servers, List<String> players) {
        return servers.stream()
                .filter(s -> s.fits(players.size()))
                .min(Comparator.comparingDouble(GameServer::load).thenComparing(GameServer::id));
    }

    @Override
    public String name() {
        return "Least loaded";
    }
}
