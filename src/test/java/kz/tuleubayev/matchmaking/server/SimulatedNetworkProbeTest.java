package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulatedNetworkProbeTest {

    private SimulatedNetworkProbe probe(long seed) {
        return new SimulatedNetworkProbe(Map.of("Damir", "EU", "Mike", "NA"), Map.of("EU-1", "EU", "NA-1", "NA"), seed);
    }

    @Test
    void pingIsBaseForTheRegionsPlusSmallJitter() {
        SimulatedNetworkProbe p = probe(1);
        for (int i = 0; i < 50; i++) {
            int sameRegion = p.pingMs("Damir", "EU-1");
            int crossOcean = p.pingMs("Damir", "NA-1");
            assertTrue(sameRegion >= 25 && sameRegion < 40, "EU->EU " + sameRegion);
            assertTrue(crossOcean >= 110 && crossOcean < 125, "EU->NA " + crossOcean);
        }
    }

    @Test
    void measurementsAreStableForTheSamePairAndSeed() {
        SimulatedNetworkProbe p = probe(5);
        assertEquals(p.pingMs("Mike", "EU-1"), p.pingMs("Mike", "EU-1"));
        assertEquals(p.pingMs("Mike", "EU-1"), probe(5).pingMs("Mike", "EU-1"));
    }

    @Test
    void unknownPlayerOrServerHasNoRoute() {
        assertThrows(IllegalArgumentException.class, () -> probe(1).pingMs("Ghost", "EU-1"));
        assertThrows(IllegalArgumentException.class, () -> probe(1).pingMs("Damir", "MOON-1"));
    }

    @Test
    void gameServerKnowsItsLoadAndRejectsImpossibleSlots() {
        assertEquals(0.5, new GameServer("X", "EU", 10, 5).load());
        assertThrows(IllegalArgumentException.class, () -> new GameServer("X", "EU", 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new GameServer("X", "EU", 10, 11));
        assertThrows(IllegalArgumentException.class, () -> new GameServer("X", "EU", 10, -1));
        assertTrue(new GameServer("X", "EU", 10, 5).toString().contains("5/10"));
    }
}
