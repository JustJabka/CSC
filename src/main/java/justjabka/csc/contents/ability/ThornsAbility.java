package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class ThornsAbility extends BaseActiveAbility {
    private final AttributeModifier damageReflectionModifier;
    private AttributeInstance damageReflectionPercentInstance;

    public ThornsAbility(Identifier key, int duration, AttributeModifier damageReflectionModifier) {
        super(key, duration);
        this.damageReflectionModifier = damageReflectionModifier;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        damageReflectionPercentInstance = player.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);
        damageReflectionPercentInstance.addTransientModifier(damageReflectionModifier);

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
        damageReflectionPercentInstance.removeModifier(key);
    }
}