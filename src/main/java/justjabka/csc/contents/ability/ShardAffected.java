package justjabka.csc.contents.ability;

import justjabka.csc.handlers.TrinketHandler;
import net.minecraft.world.entity.player.Player;

public interface ShardAffected {

    default boolean hasShard(Player player) {
        return TrinketHandler.hasShard(player);
    }
}
