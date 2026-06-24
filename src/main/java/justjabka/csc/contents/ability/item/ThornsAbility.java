package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class ThornsAbility extends BaseActiveAbility {
    private final AttributeModifier DAMAGE_REFLECTION_MODIFIER = new AttributeModifier(
            getId(),
            0.5,
            AttributeModifier.Operation.ADD_VALUE
    );

    public ThornsAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifier(
                player,
                CSCAttributes.DAMAGE_REFLECTION_PERCENT,
                DAMAGE_REFLECTION_MODIFIER
        );

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        if (!(ctx.player instanceof ServerPlayer serverPlayer)) return;

        serverPlayer.level().sendParticles (
                ParticleTypes.WITCH,
                serverPlayer.getX(),
                serverPlayer.getBoundingBox().minY + 1,
                serverPlayer.getZ(),
                5,
                0.1, 0.5, 0.1,
                1
        );
    }

    @Override
    public void onEnd() {
        AttributeHandler.removeModifier(
                ctx.player,
                CSCAttributes.DAMAGE_REFLECTION_PERCENT,
                DAMAGE_REFLECTION_MODIFIER
        );
    }
}