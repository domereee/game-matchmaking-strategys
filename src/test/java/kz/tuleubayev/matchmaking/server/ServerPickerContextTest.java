package kz.tuleubayev.matchmaking.server;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerPickerContextTest {

    @Test
    void delegatesAndCanSwitchStrategyAtRuntime() {
        ServerPicker picker = new ServerPicker(new FillFirstSelection());
        assertEquals("EU-1", picker.pick(Servers.ALL, Servers.DUO).orElseThrow().id());

        picker.setStrategy(new LeastLoadedSelection());
        assertEquals("ASIA-1", picker.pick(Servers.ALL, Servers.DUO).orElseThrow().id());
        assertEquals("Least loaded", picker.strategyName());
    }

    @Test
    void anyNewStrategyPlugsInWithoutChangingTheContext() {
        ServerSelectionStrategy alwaysLast = new ServerSelectionStrategy() {
            @Override
            public Optional<GameServer> choose(List<GameServer> servers, List<String> players) {
                return servers.isEmpty() ? Optional.empty() : Optional.of(servers.get(servers.size() - 1));
            }

            @Override
            public String name() {
                return "Always last";
            }
        };
        assertEquals("ASIA-1", new ServerPicker(alwaysLast).pick(Servers.ALL, Servers.DUO).orElseThrow().id());
    }

    @Test
    void badInputIsRejected() {
        ServerPicker picker = new ServerPicker(new LeastLoadedSelection());
        assertThrows(IllegalArgumentException.class, () -> picker.pick(Servers.ALL, List.of()));
        assertThrows(IllegalArgumentException.class, () -> picker.pick(Servers.ALL, null));
        assertThrows(IllegalArgumentException.class, () -> picker.pick(Servers.ALL, List.of("Dana", "dana")));
        assertThrows(NullPointerException.class, () -> picker.setStrategy(null));
        assertThrows(NullPointerException.class, () -> new LowestPingSelection(null));
    }
}
