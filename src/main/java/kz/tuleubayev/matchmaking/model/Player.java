package kz.tuleubayev.matchmaking.model;

import java.util.Objects;

/**
 * A player waiting in a lobby.
 *
 * @param nickname unique in-game name
 * @param rating   skill rating (MMR)
 * @param partyId  id of the premade party the player queued with; {@code null} for a solo player
 */
public record Player(String nickname, int rating, String partyId) {

    public Player {
        Objects.requireNonNull(nickname, "nickname");
    }

    public static Player solo(String nickname, int rating) {
        return new Player(nickname, rating, null);
    }

    public boolean isSolo() {
        return partyId == null;
    }

    @Override
    public String toString() {
        return nickname + "(" + rating + (isSolo() ? "" : ", party " + partyId) + ")";
    }
}
