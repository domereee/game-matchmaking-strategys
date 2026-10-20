package kz.tuleubayev.matchmaking.server;

import kz.tuleubayev.matchmaking.model.GameMode;

import java.time.Clock;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Chooses a server-selection strategy from the current situation: game mode and time of day.
 * The clock is injected so tests can set any hour.
 * <pre>
 *   RANKED game, any time      -> Lowest ping   (fairness first)
 *   18:00 - 23:59 (peak)       -> Least loaded  (spread the evening crowd)
 *   00:00 - 06:59 (night)      -> Fill first    (pack players, switch idle servers off)
 *   otherwise (day)            -> Lowest ping
 * </pre>
 */
public class ServerSelectionSelector {

    public static final int PEAK_STARTS = 18;
    public static final int NIGHT_ENDS = 7;

    private final Clock clock;
    private final ServerSelectionStrategy lowestPing;
    private final ServerSelectionStrategy leastLoaded = new LeastLoadedSelection();
    private final ServerSelectionStrategy fillFirst = new FillFirstSelection();

    public ServerSelectionSelector(LatencyProvider latency, Clock clock) {
        this.lowestPing = new LowestPingSelection(latency);
        this.clock = Objects.requireNonNull(clock);
    }

    public ServerSelectionStrategy choose(GameMode mode) {
        Objects.requireNonNull(mode, "mode");
        if (mode == GameMode.RANKED) {
            return lowestPing;
        }
        int hour = LocalTime.now(clock).getHour();
        if (hour >= PEAK_STARTS) {
            return leastLoaded;
        }
        if (hour < NIGHT_ENDS) {
            return fillFirst;
        }
        return lowestPing;
    }
}
