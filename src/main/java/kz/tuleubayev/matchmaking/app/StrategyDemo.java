package kz.tuleubayev.matchmaking.app;

import kz.tuleubayev.matchmaking.balancing.GreedyBalancing;
import kz.tuleubayev.matchmaking.balancing.PartyPreservingBalancing;
import kz.tuleubayev.matchmaking.balancing.RandomBalancing;
import kz.tuleubayev.matchmaking.balancing.SnakeDraftBalancing;
import kz.tuleubayev.matchmaking.balancing.TeamBalancer;
import kz.tuleubayev.matchmaking.balancing.TeamBalancingSelector;
import kz.tuleubayev.matchmaking.balancing.TeamBalancingStrategy;
import kz.tuleubayev.matchmaking.model.GameMode;
import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.server.FillFirstSelection;
import kz.tuleubayev.matchmaking.server.GameServer;
import kz.tuleubayev.matchmaking.server.LatencyProvider;
import kz.tuleubayev.matchmaking.server.LeastLoadedSelection;
import kz.tuleubayev.matchmaking.server.LowestPingSelection;
import kz.tuleubayev.matchmaking.server.ServerPicker;
import kz.tuleubayev.matchmaking.server.ServerSelectionSelector;
import kz.tuleubayev.matchmaking.server.ServerSelectionStrategy;
import kz.tuleubayev.matchmaking.server.SimulatedNetworkProbe;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * Demo of both strategy families:
 * <ol>
 *   <li>the same input processed by every strategy of a family,</li>
 *   <li>manual choice vs. automatic choice by the Selector,</li>
 *   <li>switching the strategy of a context at runtime.</li>
 * </ol>
 * Run: {@code java -jar target/strategy-demo.jar} (optionally {@code --at 21:30} to pretend it is that time).
 */
public final class StrategyDemo {

    static final List<Player> LOBBY = List.of(
            new Player("Damir", 2100, "D1"), new Player("Aruzhan", 2000, "D1"),
            Player.solo("Timur", 1900), Player.solo("Dana", 1800),
            Player.solo("Aliya", 1700), Player.solo("Mike", 1600),
            Player.solo("Kenji", 1500), Player.solo("Nurlan", 2400));

    static final List<GameServer> SERVERS = List.of(
            new GameServer("EU-1", "EU", 10, 2),
            new GameServer("EU-2", "EU", 10, 9),
            new GameServer("NA-1", "NA", 10, 4),
            new GameServer("ASIA-1", "ASIA", 6, 6));

    static final List<String> PARTY = List.of("Damir", "Aruzhan", "Mike");

    private StrategyDemo() {
    }

    public static void main(String[] args) {
        LocalTime now = LocalTime.now();
        if (args.length == 2 && args[0].equals("--at")) {
            now = LocalTime.parse(args[1]);
        }
        LatencyProvider network = new SimulatedNetworkProbe(
                Map.of("Damir", "EU", "Aruzhan", "EU", "Timur", "EU", "Dana", "EU", "Aliya", "EU",
                        "Nurlan", "EU", "Mike", "NA", "Kenji", "ASIA"),
                Map.of("EU-1", "EU", "EU-2", "EU", "NA-1", "NA", "ASIA-1", "ASIA"), 7);

        title("Video Game Matchmaking - Strategy pattern demo (Damir Tuleubayev)");
        System.out.println("Lobby:   " + LOBBY);
        System.out.println("Servers: " + SERVERS);
        System.out.println("Party looking for a server: " + PARTY + " (two players in EU, Mike in NA)");

        title("1a. Same lobby, every team-balancing strategy");
        List<TeamBalancingStrategy> balancers = List.of(new SnakeDraftBalancing(), new GreedyBalancing(),
                new PartyPreservingBalancing(), new RandomBalancing(42));
        for (TeamBalancingStrategy s : balancers) {
            System.out.printf("  %-17s %s%n", s.name(), new TeamBalancer(s).balance(LOBBY));
        }

        title("1b. Same party, every server-selection strategy");
        List<ServerSelectionStrategy> pickers = List.of(new LowestPingSelection(network), new LeastLoadedSelection(),
                new FillFirstSelection());
        for (ServerSelectionStrategy s : pickers) {
            System.out.printf("  %-13s -> %s%n", s.name(),
                    new ServerPicker(s).pick(SERVERS, PARTY).map(GameServer::toString).orElse("(no server)"));
        }

        title("2. Manual choice vs. automatic choice by the Selector");
        System.out.println("  Manual:  operator forces Greedy  -> "
                + new TeamBalancer(new GreedyBalancing()).balance(LOBBY));
        TeamBalancingSelector teamSelector = new TeamBalancingSelector(42);
        for (GameMode mode : GameMode.values()) {
            TeamBalancingStrategy chosen = teamSelector.choose(mode, LOBBY);
            System.out.printf("  Auto %-7s (lobby has a party) -> %-17s %s%n", mode, chosen.name(),
                    new TeamBalancer(chosen).balance(LOBBY));
        }
        TeamBalancingStrategy soloChoice = teamSelector.choose(GameMode.CASUAL, LOBBY.subList(2, 8));
        System.out.println("  Auto CASUAL (all solo)         -> " + soloChoice.name());
        System.out.println();
        for (LocalTime t : List.of(LocalTime.of(3, 0), LocalTime.of(13, 0), LocalTime.of(20, 30), now)) {
            Clock clock = Clock.fixed(LocalDate.of(2026, 10, 19).atTime(t).atZone(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault());
            ServerSelectionSelector serverSelector = new ServerSelectionSelector(network, clock);
            for (GameMode mode : List.of(GameMode.CASUAL, GameMode.RANKED)) {
                ServerSelectionStrategy chosen = serverSelector.choose(mode);
                System.out.printf("  Auto at %s %-7s -> %-13s -> %s%s%n", t.withNano(0), mode, chosen.name(),
                        new ServerPicker(chosen).pick(SERVERS, PARTY).map(GameServer::id).orElse("(none)"),
                        t == now ? "   <- current time" : "");
            }
        }

        title("3. Switching the strategy of a context at runtime");
        TeamBalancer balancer = new TeamBalancer(new SnakeDraftBalancing());
        System.out.println("  TeamBalancer with " + balancer.strategyName() + ": " + balancer.balance(LOBBY));
        balancer.setStrategy(new PartyPreservingBalancing());
        System.out.println("  setStrategy(Party preserving):    " + balancer.balance(LOBBY));
        ServerPicker picker = new ServerPicker(new FillFirstSelection());
        System.out.println("  ServerPicker with " + picker.strategyName() + ": " + picker.pick(SERVERS, PARTY).orElseThrow());
        picker.setStrategy(new LeastLoadedSelection());
        System.out.println("  setStrategy(Least loaded):       " + picker.pick(SERVERS, PARTY).orElseThrow());
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
