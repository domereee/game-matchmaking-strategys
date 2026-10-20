package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The same contract checked on every strategy of family 2. */
class AllServerStrategiesTest {

    static Stream<ServerSelectionStrategy> strategies() {
        return Stream.of(new LowestPingSelection(Servers.duoPings()), new LeastLoadedSelection(),
                new FillFirstSelection());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("strategies")
    void theChosenServerComesFromTheListAndHasRoomForEveryone(ServerSelectionStrategy strategy) {
        GameServer chosen = strategy.choose(Servers.ALL, Servers.DUO).orElseThrow();
        assertTrue(Servers.ALL.contains(chosen), strategy.name());
        assertTrue(chosen.freeSlots() >= Servers.DUO.size(), strategy.name() + " picked a full server");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("strategies")
    void noServerIsChosenWhenNobodyHasRoom(ServerSelectionStrategy strategy) {
        List<GameServer> allFull = List.of(new GameServer("EU-1", "EU", 10, 1), new GameServer("NA-1", "NA", 10, 0));
        assertEquals(Optional.empty(), strategy.choose(allFull, Servers.DUO), strategy.name());
        assertEquals(Optional.empty(), strategy.choose(List.of(), Servers.DUO), strategy.name());
    }
}
