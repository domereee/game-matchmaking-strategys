package kz.tuleubayev.matchmaking.server;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Asks the {@link LatencyProvider} for every player's ping to every server with room and picks
 * the server with the lowest average ping. Best for fair, competitive games.
 */
public class LowestPingSelection implements ServerSelectionStrategy {

    private final LatencyProvider latency;

    public LowestPingSelection(LatencyProvider latency) {
        this.latency = Objects.requireNonNull(latency);
    }

    @Override
    public Optional<GameServer> choose(List<GameServer> servers, List<String> players) {
        return servers.stream()
                .filter(s -> s.fits(players.size()))
                .min(Comparator.comparingDouble((GameServer s) -> averagePing(s, players))
                        .thenComparing(GameServer::id));
    }

    double averagePing(GameServer server, List<String> players) {
        return players.stream().mapToInt(p -> latency.pingMs(p, server.id())).average().orElse(0);
    }

    @Override
    public String name() {
        return "Lowest ping";
    }
}
