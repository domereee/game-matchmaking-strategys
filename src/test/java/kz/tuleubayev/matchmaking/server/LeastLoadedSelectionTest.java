package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeastLoadedSelectionTest {

    private final LeastLoadedSelection leastLoaded = new LeastLoadedSelection();

    @Test
    void picksTheEmptiestServerByShareOfUsedSlots() {
        assertEquals("ASIA-1", leastLoaded.choose(Servers.ALL, Servers.DUO).orElseThrow().id());
    }

    @Test
    void comparesLoadNotRawFreeSlots() {
        GameServer big = new GameServer("BIG", "EU", 100, 50);   // 50% used, 50 free
        GameServer small = new GameServer("SMALL", "EU", 10, 9); // 10% used, 9 free
        assertEquals("SMALL", leastLoaded.choose(List.of(big, small), Servers.DUO).orElseThrow().id());
        assertEquals("Least loaded", leastLoaded.name());
    }
}
