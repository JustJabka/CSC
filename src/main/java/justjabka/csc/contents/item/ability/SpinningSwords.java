package justjabka.csc.contents.item.ability;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

import static justjabka.csc.handlers.DescriptionHandler.*;

public class SpinningSwords extends BaseActiveTrinketItem {
    public static final double RADIUS = 1.5;
    public static final double DAMAGE_PERCENT = 0.08;
    public static final double DAMAGE_PERCENT_SHARD_BONUS = 0.02;

    public SpinningSwords(Properties properties) {
        super(properties.useCooldown(32)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.SPINNING_SWORDS.getId(), TimeHandler.secondsToTicks(5))
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.spinning_swords.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable(
                "item.csc.spinning_swords.description.2",
                wrapDecimalAsPercent(DAMAGE_PERCENT),
                MAGICAL_DAMAGE,
                RADIUS,
                MAX_HEALTH
        ).withStyle(ChatFormatting.GRAY));
    }
}
