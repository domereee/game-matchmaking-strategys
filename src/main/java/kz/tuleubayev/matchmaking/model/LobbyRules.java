package kz.tuleubayev.matchmaking.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Checks the input of both contexts before any strategy runs. */
public final class LobbyRules {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 10;

    private LobbyRules() {
    }

    public static void requireValid(List<Player> lobby) {
        if (lobby == null || lobby.size() < MIN_PLAYERS || lobby.size() > MAX_PLAYERS) {
            throw new IllegalArgumentException("A lobby needs " + MIN_PLAYERS + ".." + MAX_PLAYERS + " players");
        }
        if (lobby.size() % 2 != 0) {
            throw new IllegalArgumentException("A lobby needs an even number of players, got " + lobby.size());
        }
        requireUnique(lobby.stream().map(Player::nickname).toList());
    }

    /** A group looking for a server must have at least one player, each listed once. */
    public static void requirePlayers(List<String> players) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("At least one player is needed");
        }
        requireUnique(players);
    }

    private static void requireUnique(List<String> nicknames) {
        Set<String> seen = new HashSet<>();
        for (String nick : nicknames) {
            if (!seen.add(nick.toLowerCase())) {
                throw new IllegalArgumentException("Player " + nick + " is listed twice");
            }
        }
    }
}
