package kz.tuleubayev.matchmaking.server;

import java.util.List;

/** Server fleet shared by the server-selection tests. */
final class Servers {

    private Servers() {
    }

    static final GameServer EU_1 = new GameServer("EU-1", "EU", 10, 2);      // busy
    static final GameServer EU_2 = new GameServer("EU-2", "EU", 10, 9);      // quiet
    static final GameServer NA_1 = new GameServer("NA-1", "NA", 10, 4);      // half full
    static final GameServer ASIA_1 = new GameServer("ASIA-1", "ASIA", 6, 6); // empty

    static final List<GameServer> ALL = List.of(EU_1, EU_2, NA_1, ASIA_1);

    static final List<String> DUO = List.of("Damir", "Mike");

    /** Damir lives in EU, Mike in NA. */
    static FixedLatency duoPings() {
        return FixedLatency.of()
                .ping("Damir", "EU-1", 30).ping("Mike", "EU-1", 100)    // avg 65
                .ping("Damir", "EU-2", 40).ping("Mike", "EU-2", 105)    // avg 72.5
                .ping("Damir", "NA-1", 120).ping("Mike", "NA-1", 20)    // avg 70
                .ping("Damir", "ASIA-1", 180).ping("Mike", "ASIA-1", 150); // avg 165
    }
}
