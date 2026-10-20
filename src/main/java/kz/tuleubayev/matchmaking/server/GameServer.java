package kz.tuleubayev.matchmaking.server;

/**
 * A dedicated game server.
 *
 * @param id        e.g. EU-1
 * @param region    data-centre region
 * @param capacity  total player slots
 * @param freeSlots slots not used right now
 */
public record GameServer(String id, String region, int capacity, int freeSlots) {

    public GameServer {
        if (capacity <= 0 || freeSlots < 0 || freeSlots > capacity) {
            throw new IllegalArgumentException("Bad slots for " + id + ": " + freeSlots + "/" + capacity);
        }
    }

    public boolean fits(int players) {
        return freeSlots >= players;
    }

    /** 0.0 = empty server, 1.0 = full server. */
    public double load() {
        return 1.0 - (double) freeSlots / capacity;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %d/%d free)", id, region, freeSlots, capacity);
    }
}
