package kz.tuleubayev.matchmaking.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Fake {@link LatencyProvider} for tests: returns exactly the pings we wrote down. Asking for a
 * pair nobody set up is a test bug, so it fails loudly.
 */
final class FixedLatency implements LatencyProvider {

    private final Map<String, Integer> pings = new HashMap<>();

    static FixedLatency of() {
        return new FixedLatency();
    }

    FixedLatency ping(String nickname, String serverId, int ms) {
        pings.put(nickname + "@" + serverId, ms);
        return this;
    }

    @Override
    public int pingMs(String nickname, String serverId) {
        Integer ms = pings.get(nickname + "@" + serverId);
        if (ms == null) {
            throw new AssertionError("test did not define ping for " + nickname + "@" + serverId);
        }
        return ms;
    }
}
