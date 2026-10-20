package kz.tuleubayev.matchmaking.server;

import kz.tuleubayev.matchmaking.model.GameMode;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** One test per selector rule, plus the hour boundaries. The clock is fixed for every test. */
class ServerSelectionSelectorTest {

    private static ServerSelectionSelector at(String time) {
        Clock clock = Clock.fixed(Instant.parse("2026-10-19T" + time + ":00Z"), ZoneOffset.UTC);
        return new ServerSelectionSelector(Servers.duoPings(), clock);
    }

    @Test
    void rankedAlwaysUsesLowestPingEvenAtPeak() {
        assertInstanceOf(LowestPingSelection.class, at("20:00").choose(GameMode.RANKED));
        assertInstanceOf(LowestPingSelection.class, at("03:00").choose(GameMode.RANKED));
    }

    @Test
    void eveningPeakSpreadsLoad() {
        assertInstanceOf(LeastLoadedSelection.class, at("20:00").choose(GameMode.CASUAL));
        assertInstanceOf(LeastLoadedSelection.class, at("18:00").choose(GameMode.CUSTOM));
        assertInstanceOf(LeastLoadedSelection.class, at("23:59").choose(GameMode.CASUAL));
    }

    @Test
    void nightPacksPlayersOntoFewServers() {
        assertInstanceOf(FillFirstSelection.class, at("03:00").choose(GameMode.CASUAL));
        assertInstanceOf(FillFirstSelection.class, at("00:00").choose(GameMode.CASUAL));
        assertInstanceOf(FillFirstSelection.class, at("06:59").choose(GameMode.CASUAL));
    }

    @Test
    void daytimeUsesLowestPing() {
        assertInstanceOf(LowestPingSelection.class, at("07:00").choose(GameMode.CASUAL));
        assertInstanceOf(LowestPingSelection.class, at("12:00").choose(GameMode.CASUAL));
        assertInstanceOf(LowestPingSelection.class, at("17:59").choose(GameMode.CASUAL));
    }

    @Test
    void modeIsRequired() {
        assertThrows(NullPointerException.class, () -> at("12:00").choose(null));
    }
}
