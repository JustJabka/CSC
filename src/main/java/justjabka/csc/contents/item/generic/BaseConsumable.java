package justjabka.csc.contents.item.generic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseConsumable extends BaseItem {
    public BaseConsumable(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (isClientSide(player)) return InteractionResult.PASS;
        if (!canActivate(player, stack, level)) return InteractionResult.PASS;

        stack.consume(1, player);
        onUse(level, player, hand, stack);

        return InteractionResult.SUCCESS;
    }

    protected boolean canActivate(Player player, ItemStack stack, Level level) {
        return true;
    }

    protected abstract void onUse(
            Level level,
            Player player,
            InteractionHand hand,
            ItemStack stack
    );
}
