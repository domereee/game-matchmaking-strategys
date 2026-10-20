package kz.tuleubayev.matchmaking.server;

import java.util.Map;
import java.util.Objects;

/**
 * Simulated external data source: pretends to ping each server from each player's client. Base
 * ping depends on the two regions, plus 0-14 ms of jitter that is stable for each player/server
 * pair (so repeated measurements agree). No real network is used.
 */
public class SimulatedNetworkProbe implements LatencyProvider {

    private static final Map<String, Integer> BASE_MS = Map.of(
            "EU-EU", 25, "NA-NA", 25, "ASIA-ASIA", 25,
            "EU-NA", 110, "NA-EU", 110,
            "EU-ASIA", 170, "ASIA-EU", 170,
            "NA-ASIA", 150, "ASIA-NA", 150);

    private final Map<String, String> playerRegions;
    private final Map<String, String> serverRegions;
    private final long seed;

    public SimulatedNetworkProbe(Map<String, String> playerRegions, Map<String, String> serverRegions, long seed) {
        this.playerRegions = Map.copyOf(playerRegions);
        this.serverRegions = Map.copyOf(serverRegions);
        this.seed = seed;
    }

    @Override
    public int pingMs(String nickname, String serverId) {
        String from = playerRegions.get(nickname);
        String to = serverRegions.get(serverId);
        Integer base = BASE_MS.get(from + "-" + to);
        if (base == null) {
            throw new IllegalArgumentException("No route from " + nickname + " to " + serverId);
        }
        return base + Math.floorMod(Objects.hash(nickname, serverId, seed), 15);
    }
}
