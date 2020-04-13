package de.mmagic.anstoss.resource;

import de.mmagic.anstoss.model.Player;

import java.util.function.Function;

public class PlayerConverter implements Function<Player, PlayerRest> {

    @Override
    public PlayerRest apply(Player player) {
        return new PlayerRest(player.id, player.name, player.age, player.strength, player.position, player.country, player.price, player.days);
    }
}
