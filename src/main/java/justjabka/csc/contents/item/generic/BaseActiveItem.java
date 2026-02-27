package justjabka.csc.contents.item.generic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BaseActiveItem extends Item {
    public BaseActiveItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    protected int getSecondsToTicks(int seconds) {
        return seconds * 20;
    }

    protected boolean isClientSide(Player player) {
        return player.level().isClientSide();
    }

    protected boolean isOnCooldown(Player player, ItemStack stack) {
        return player.getCooldowns().isOnCooldown(stack);
    }
}