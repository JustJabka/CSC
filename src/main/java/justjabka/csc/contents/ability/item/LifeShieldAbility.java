package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.Map;
import java.util.function.Consumer;

public class LifeShieldAbility extends BaseActiveAbility {
    private static final double DAMAGE_MULTIPLIER_MODIFIER = -0.05;
    private static final float HEAL_AMOUNT = 10;
    private static final float ABSORPTION_AMOUNT = 8;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                    getId(),
                    DAMAGE_MULTIPLIER_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MAX_ABSORPTION, new AttributeModifier(
                    getId(),
                    ABSORPTION_AMOUNT,
                    AttributeModifier.Operation.ADD_VALUE
            )
    );

    public LifeShieldAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.1", HEAL_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.2", ABSORPTION_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.3", DescriptionHandler.wrapDecimalAsPercent(DAMAGE_MULTIPLIER_MODIFIER)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifiers(player, ACTIVE_MODIFIERS);

        player.heal(HEAL_AMOUNT);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + ABSORPTION_AMOUNT);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        AttributeHandler.removeModifiers(ctx.player, ACTIVE_MODIFIERS);
    }
}
