package kz.tuleubayev.matchmaking.balancing;

import kz.tuleubayev.matchmaking.model.Player;
import kz.tuleubayev.matchmaking.model.Teams;

import java.util.List;

/** Test lobbies shared by the balancing tests. */
final class Lobbies {

    private Lobbies() {
    }

    static final List<Player> FOUR_SOLO = List.of(
            Player.solo("A", 2000), Player.solo("B", 1800), Player.solo("C", 1600), Player.solo("D", 1400));

    static final List<Player> EIGHT_SOLO = List.of(
            Player.solo("P1", 2400), Player.solo("P2", 2200), Player.solo("P3", 2000), Player.solo("P4", 1900),
            Player.solo("P5", 1800), Player.solo("P6", 1600), Player.solo("P7", 1500), Player.solo("P8", 1200));

    /** Two premade duos plus four solo players. */
    static final List<Player> TWO_DUOS = List.of(
            new Player("Damir", 2100, "D1"), new Player("Aruzhan", 2000, "D1"),
            new Player("Timur", 1900, "D2"), new Player("Dana", 1800, "D2"),
            Player.solo("Aliya", 1700), Player.solo("Mike", 1600),
            Player.solo("Kenji", 1500), Player.solo("Nurlan", 1400));

    static List<String> names(List<Player> team) {
        return team.stream().map(Player::nickname).toList();
    }

    static boolean sameTeam(Teams t, String a, String b) {
        List<String> blue = names(t.blue());
        return blue.contains(a) == blue.contains(b);
    }
}
