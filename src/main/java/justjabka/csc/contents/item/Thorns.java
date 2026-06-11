package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.item.ThornsAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Thorns extends BaseActiveTrinketItem {
    private final AttributeModifier DAMAGE_REFLECTION_MODIFIER = new AttributeModifier(
            getKey(),
            0.5,
            AttributeModifier.Operation.ADD_VALUE
    );

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "thorns");
    }

    @Override
    protected int getCooldown() {
        return 45;
    }

    @Override
    protected int getDuration() {
        return 5;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new ThornsAbility(
                getKey(),
                getSecondsToTicks(getDuration()),
                DAMAGE_REFLECTION_MODIFIER
        );
    }

    public Thorns(Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);

        String reflectionPercent = wrapDecimalAsPercent(DAMAGE_REFLECTION_MODIFIER.amount());
        textConsumer.accept(Component.translatable("item.csc.thorns.description", reflectionPercent, MAGICAL_DAMAGE).withStyle(ChatFormatting.GRAY));
    }
}