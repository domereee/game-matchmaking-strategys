package kz.tuleubayev.matchmaking.model;

import java.util.List;

/** The result of balancing a lobby: two teams of equal size. */
public record Teams(List<Player> blue, List<Player> red) {

    public Teams {
        blue = List.copyOf(blue);
        red = List.copyOf(red);
    }

    public int blueRating() {
        return blue.stream().mapToInt(Player::rating).sum();
    }

    public int redRating() {
        return red.stream().mapToInt(Player::rating).sum();
    }

    /** Absolute difference of the summed ratings: 0 is a perfectly fair match. */
    public int ratingGap() {
        return Math.abs(blueRating() - redRating());
    }

    @Override
    public String toString() {
        return String.format("BLUE %d %s  vs  RED %d %s  (gap %d)", blueRating(), blue, redRating(), red, ratingGap());
    }
}
