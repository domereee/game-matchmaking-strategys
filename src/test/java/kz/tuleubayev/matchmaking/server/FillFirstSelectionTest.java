package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FillFirstSelectionTest {

    private final FillFirstSelection fillFirst = new FillFirstSelection();

    @Test
    void picksTheServerThatIsLeftFullest() {
        // leftover after a duo joins: EU-1 0, EU-2 7, NA-1 2, ASIA-1 4
        assertEquals("EU-1", fillFirst.choose(Servers.ALL, Servers.DUO).orElseThrow().id());
    }

    @Test
    void movesOnWhenTheTightestServerHasNoRoom() {
        List<String> trio = List.of("A", "B", "C");
        // EU-1 has 2 free: no room. Leftover: EU-2 6, NA-1 1, ASIA-1 3
        assertEquals("NA-1", fillFirst.choose(Servers.ALL, trio).orElseThrow().id());
        assertEquals("Fill first", fillFirst.name());
    }
}
