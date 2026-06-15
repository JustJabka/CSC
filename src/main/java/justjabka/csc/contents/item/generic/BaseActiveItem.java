package justjabka.csc.contents.item.generic;

import justjabka.csc.types.AbilityContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class BaseActiveItem extends BaseItem implements ActivatableItem {
    public BaseActiveItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(
            @NonNull ItemStack stack,
            @NonNull TooltipContext context,
            @NonNull TooltipDisplay displayComponent,
            Consumer<Component> textConsumer,
            @NonNull TooltipFlag type
    ) {
        getDescription(stack, context, displayComponent, textConsumer, type);
    }

    // Interactions
    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot());
        return tryActivate(ctx);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack item,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot(), item, target);
        return tryActivate(ctx);
    }
}