package justjabka.csc.handlers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AbilityContext {
    public final Player player;
    public final InteractionHand hand;
    public final Level level;

    public AbilityContext(Player player, InteractionHand hand) {
        this.player = player;
        this.hand = hand;
        this.level = player.level();
    }

    public ItemStack getStack() {
        return player.getItemInHand(hand);
    }
}