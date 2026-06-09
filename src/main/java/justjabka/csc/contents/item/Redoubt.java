package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.RedoubtAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Redoubt extends BaseActiveTrinketItem {
    private static final double INCOMING_DAMAGE_MULTIPLIER_MODIFIER = -0.35;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = 1;

    public Redoubt(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "redoubt");
    }

    @Override
    protected int getCooldown() {
        return 45;
    }

    @Override
    protected int getDuration() {
        return 7;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return new RedoubtAbility(
                getKey(),
                getSecondsToTicks(getDuration()),
                INCOMING_DAMAGE_MULTIPLIER_MODIFIER,
                KNOCKBACK_RESISTANCE_MODIFIER
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.redoubt.description.1", wrapDecimalAsPercent(INCOMING_DAMAGE_MULTIPLIER_MODIFIER)).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.redoubt.description.2", wrapDecimalAsPercent(KNOCKBACK_RESISTANCE_MODIFIER)).withStyle(ChatFormatting.GRAY));
    }
}
