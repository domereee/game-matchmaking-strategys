package kz.tuleubayev.matchmaking.server;

import kz.tuleubayev.matchmaking.model.LobbyRules;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Context of strategy family 2. Holds one {@link ServerSelectionStrategy}, lets you swap it at
 * runtime and delegates to it without ever asking which strategy it holds.
 */
public class ServerPicker {

    private ServerSelectionStrategy strategy;

    public ServerPicker(ServerSelectionStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public void setStrategy(ServerSelectionStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public String strategyName() {
        return strategy.name();
    }

    public Optional<GameServer> pick(List<GameServer> servers, List<String> players) {
        LobbyRules.requirePlayers(players);
        return strategy.choose(servers, players);
    }
}
