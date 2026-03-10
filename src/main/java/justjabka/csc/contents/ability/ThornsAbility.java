package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ThornsAbility extends ActiveAbility {
    private final double damageReflection;

    private AttributeInstance attribute;
    private static final Identifier ABILITY_DAMAGE_REFLECTION_PERCENT_ID = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "thorns_ability");

    public ThornsAbility(int duration, double damageReflection) {
        super(false, duration);
        this.damageReflection = damageReflection;
    }

    @Override
    public void onStart() {
        attribute = ctx.player.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);

        attribute.addTransientModifier(
                new AttributeModifier(
                        ABILITY_DAMAGE_REFLECTION_PERCENT_ID,
                        damageReflection,
                        AttributeModifier.Operation.ADD_VALUE
                ));

        // Play sound
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        if (!(ctx.player instanceof ServerPlayer serverPlayer)) return;

        // Particles
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
        attribute.removeModifier(ABILITY_DAMAGE_REFLECTION_PERCENT_ID);
    }
}