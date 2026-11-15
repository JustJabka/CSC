package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.handlers.TeamHandler;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class LifeShieldAbility extends Ability {
    private final List<UUID> BUFFED_PLAYERS = new ArrayList<>();

    private static final double DAMAGE_MULTIPLIER_MODIFIER = -0.05;
    private static final float HEAL_AMOUNT = 10;
    private static final float ABSORPTION_AMOUNT = 8;
    private static final double RADIUS = 10;

    public LifeShieldAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, DAMAGE_MULTIPLIER_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.MAX_ABSORPTION, ABSORPTION_AMOUNT, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.1", HEAL_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.2", ABSORPTION_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.3", DescriptionHandler.wrapDecimalAsPercent(DAMAGE_MULTIPLIER_MODIFIER)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart(AbilityContext ctx) {
        giveEffects(ctx);
    }

    @Override
    public void onEnd(AbilityContext ctx) {
        clearEffects(ctx);
    }
    
    private void giveEffects(AbilityContext ctx) {
        List<Player> teammates = TeamHandler.getPlayerTeammatesInRadius(ctx.player, RADIUS);

        for (Player player : teammates) {
            BUFFED_PLAYERS.add(player.getUUID());

            addAttributeModifiers(player.getAttributes());

            player.heal(HEAL_AMOUNT);
            player.setAbsorptionAmount(player.getAbsorptionAmount() + ABSORPTION_AMOUNT);
        }
    }
    
    private void clearEffects(AbilityContext ctx) {
        for (UUID pid : BUFFED_PLAYERS) {
            if (pid == null) continue;

            Player player = ctx.level.getPlayerByUUID(pid);
            if (player == null) continue;

            removeAttributeModifiers(player.getAttributes());
        }

        BUFFED_PLAYERS.clear();
    }
}
