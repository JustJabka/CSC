package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class DarkCapeAbility extends Ability {
    private final double DAMAGE_MULTIPLIER = 2;

    private static final double VULNERABILITY_MODIFIER = 0.02;
    private static final double SPEED_MODIFIER = 0.15;

    public DarkCapeAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, VULNERABILITY_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.1",
                        DescriptionHandler.wrapDecimalAsPercent(SPEED_MODIFIER),
                        DescriptionHandler.wrapDecimalAsPercent(VULNERABILITY_MODIFIER)
                ).withStyle(ChatFormatting.GRAY)
        );
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.2",
                        DAMAGE_MULTIPLIER,
                        DescriptionHandler.PHYSICAL_DAMAGE
                ).withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        player.setInvisible(true);
    }

    @Override
    public void onEnd(AbilityContext ctx) {
        Player player = ctx.player;

        player.setInvisible(false);
    }

    // TODO: move to a separate event
    public double getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }
}
