package justjabka.csc.contents.item.generic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public abstract class BaseConsumable extends Item {
    public BaseConsumable(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        if (this instanceof ShopItem shopItem) shopItem.getPriceDescription(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.level().isClientSide()) return InteractionResult.PASS;
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
