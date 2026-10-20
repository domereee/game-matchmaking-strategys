package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LowestPingSelectionTest {

    @Test
    void picksTheServerWithTheLowestAveragePing() {
        LowestPingSelection s = new LowestPingSelection(Servers.duoPings());
        assertEquals("EU-1", s.choose(Servers.ALL, Servers.DUO).orElseThrow().id());
        assertEquals(65.0, s.averagePing(Servers.EU_1, Servers.DUO));
    }

    @Test
    void theAnswerFollowsTheExternalData() {
        FixedLatency laggyEu1 = Servers.duoPings().ping("Mike", "EU-1", 200); // EU-1 avg becomes 115
        assertEquals("NA-1", new LowestPingSelection(laggyEu1).choose(Servers.ALL, Servers.DUO).orElseThrow().id());
    }

    @Test
    void serversWithoutRoomAreSkipped() {
        GameServer eu1Full = new GameServer("EU-1", "EU", 10, 1);
        List<GameServer> fleet = List.of(eu1Full, Servers.EU_2, Servers.NA_1, Servers.ASIA_1);
        assertEquals("NA-1", new LowestPingSelection(Servers.duoPings()).choose(fleet, Servers.DUO).orElseThrow().id());
        assertEquals("Lowest ping", new LowestPingSelection(Servers.duoPings()).name());
    }
}
