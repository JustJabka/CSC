package justjabka.csc.contents.item.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.SpinningSwordsAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
        super(properties);
    }

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "spinning_swords");
    }

    @Override
    public int getCooldown() {
        return TimeHandler.secondsToTicks(32);
    }

    @Override
    public int getDuration() {
        return TimeHandler.secondsToTicks(5);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new SpinningSwordsAbility(
                getKey(),
                getDuration(),
                RADIUS,
                DAMAGE_PERCENT,
                DAMAGE_PERCENT_SHARD_BONUS
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
