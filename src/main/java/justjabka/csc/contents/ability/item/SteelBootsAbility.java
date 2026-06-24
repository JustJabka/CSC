package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class SteelBootsAbility extends BaseActiveAbility {
    private static final double GRAVITY_MODIFIER = Integer.MAX_VALUE;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = Integer.MAX_VALUE;
    private static final double JUMP_STRENGTH_MODIFIER = Integer.MIN_VALUE;
    private static final double MOVEMENT_SPEED_MODIFIER = 0.25;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            Attributes.GRAVITY, new AttributeModifier(
                    getId(),
                    GRAVITY_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.JUMP_STRENGTH, new AttributeModifier(
                    getId(),
                    JUMP_STRENGTH_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                    getId(),
                    KNOCKBACK_RESISTANCE_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    getId(),
                    MOVEMENT_SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
    );

    public SteelBootsAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifiers(player, ACTIVE_MODIFIERS);
        player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1f, 1.25f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;

        if (player.tickCount % 5 != 0) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        double movementLatency = 0.01;
        boolean isMoving = serverPlayer.getKnownMovement().lengthSqr() > movementLatency;

        if (!isMoving) return;

        serverPlayer.level().sendParticles (
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                serverPlayer.getX(),
                serverPlayer.getY() + 0.25,
                serverPlayer.getZ(),
                2,
                0.15, 0.1, 0.15,
                0.01
        );
    }

    @Override
    public void onEnd() {
        Player player = ctx.player;

        AttributeHandler.removeModifiers(player, ACTIVE_MODIFIERS);
    }
}
