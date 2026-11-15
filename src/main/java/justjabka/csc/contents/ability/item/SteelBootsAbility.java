package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class SteelBootsAbility extends Ability {
    private static final double GRAVITY_MODIFIER = Integer.MAX_VALUE;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = Integer.MAX_VALUE;
    private static final double JUMP_STRENGTH_MODIFIER = Integer.MIN_VALUE;
    private static final double MOVEMENT_SPEED_MODIFIER = 0.25;

    public SteelBootsAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(Attributes.GRAVITY, GRAVITY_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.JUMP_STRENGTH, JUMP_STRENGTH_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.2",
                Component.translatable("attribute.name.movement_speed"),
                DescriptionHandler.wrapDecimalAsPercent(MOVEMENT_SPEED_MODIFIER)
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.3").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1f, 1.25f);
    }

    @Override
    public boolean onTick(AbilityContext ctx) {
        Player player = ctx.player;

        if (player.tickCount % 5 != 0) return true;
        if (!(player instanceof ServerPlayer serverPlayer)) return true;

        double movementLatency = 0.01;
        boolean isMoving = serverPlayer.getKnownMovement().lengthSqr() > movementLatency;

        if (!isMoving) return true;

        serverPlayer.level().sendParticles (
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                serverPlayer.getX(),
                serverPlayer.getY() + 0.25,
                serverPlayer.getZ(),
                2,
                0.15, 0.1, 0.15,
                0.01
        );

        return true;
    }
}
