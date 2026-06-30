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
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class LifeShieldAbility extends BaseActiveAbility {
    private final List<UUID> BUFFED_PLAYERS = new ArrayList<>();

    private static final double DAMAGE_MULTIPLIER_MODIFIER = -0.05;
    private static final float HEAL_AMOUNT = 10;
    private static final float ABSORPTION_AMOUNT = 8;
    private static final double RADIUS = 10;

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
        giveEffects();
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        clearEffects();
    }
    
    private void giveEffects() {
        List<Player> teammates = getTeammatesInRadius();

        teammates.forEach(player -> {
            BUFFED_PLAYERS.add(player.getUUID());

            AttributeHandler.addTransientModifiers(player, ACTIVE_MODIFIERS);

            player.heal(HEAL_AMOUNT);
            player.setAbsorptionAmount(player.getAbsorptionAmount() + ABSORPTION_AMOUNT);
        });
    }
    
    private void clearEffects() {
        BUFFED_PLAYERS.forEach(pid -> {
            if (pid == null) return;

            Player player = ctx.level.getPlayerByUUID(pid);
            if (player == null) return;

            AttributeHandler.removeModifiers(player, ACTIVE_MODIFIERS);
        });

        BUFFED_PLAYERS.clear();
    }

    private List<Player> getTeammatesInRadius() {
        Player player = ctx.player;

        AABB searchBox = player.getBoundingBox().inflate(RADIUS);

        return ctx.level.getEntitiesOfClass(Player.class, searchBox, target -> {
            if (!target.isAlive()) return false;
            if (target.isSpectator()) return false;

            return target.isAlliedTo(player);
        });
    }
}
