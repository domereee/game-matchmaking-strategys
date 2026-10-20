package kz.tuleubayev.matchmaking.server;

import java.util.List;
import java.util.Optional;

/** Strategy family 2: one way of picking a game server for a group of players. */
public interface ServerSelectionStrategy {

    /** @return the chosen server, or empty when no server has room for all players */
    Optional<GameServer> choose(List<GameServer> servers, List<String> players);

    String name();
}
