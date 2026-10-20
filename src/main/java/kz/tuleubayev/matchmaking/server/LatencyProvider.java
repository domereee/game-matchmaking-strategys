package kz.tuleubayev.matchmaking.server;

/**
 * Our own boundary to the outside world (Clean Code ch. 10 "Isolating from change"): where the
 * ping numbers come from is hidden behind this interface. The strategies depend on it, never on
 * a concrete network probe, so the source can be swapped (simulated, real probe, fake in tests)
 * without touching them.
 */
public interface LatencyProvider {

    /** Round-trip time in milliseconds from the player's client to the server. */
    int pingMs(String nickname, String serverId);
}
