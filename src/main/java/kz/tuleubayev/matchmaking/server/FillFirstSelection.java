package kz.tuleubayev.matchmaking.server;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Best-fit packing: picks the server that will have the FEWEST free slots left after the group
 * joins. Keeps other servers empty so they can be switched off at night to save money.
 */
public class FillFirstSelection implements ServerSelectionStrategy {

    @Override
    public Optional<GameServer> choose(List<GameServer> servers, List<String> players) {
        return servers.stream()
                .filter(s -> s.fits(players.size()))
                .min(Comparator.comparingInt((GameServer s) -> s.freeSlots() - players.size())
                        .thenComparing(GameServer::id));
    }

    @Override
    public String name() {
        return "Fill first";
    }
}
