package justjabka.csc.contents.ability.generic;

import justjabka.csc.handlers.TrinketHandler;
import net.minecraft.world.entity.player.Player;

public interface ShardAffectedAbility {
    default boolean hasShard(Player player) {
        return TrinketHandler.hasShard(player);
    }
}
