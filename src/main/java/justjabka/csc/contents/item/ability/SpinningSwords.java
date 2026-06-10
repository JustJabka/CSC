package justjabka.csc.contents.item.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.SpinningSwordsAbility;
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

public class SpinningSwords extends BaseActiveTrinketItem {
    private static final double RADIUS = 1.5;
    private static final double DAMAGE_PERCENT = 0.12;

    public SpinningSwords(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "spinning_swords");
    }

    @Override
    protected int getCooldown() {
        return 32;
    }

    @Override
    protected int getDuration() {
        return 5;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return new SpinningSwordsAbility(
                getKey(),
                getSecondsToTicks(getDuration()),
                RADIUS,
                DAMAGE_PERCENT
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
                RADIUS
        ).withStyle(ChatFormatting.GRAY));
    }
}
