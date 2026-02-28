package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.handlers.TimedAbility;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class ThornsAbility extends TimedAbility {
    private final double damageReflection;
    AttributeInstance attribute = player.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);
    Identifier attributeIdentifier = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "thorns_ability");

    public ThornsAbility(Player player, int durationTicks, double damageReflection) {
        super(player, durationTicks);
        this.damageReflection = damageReflection;
    }

    @Override
    protected void onStart() {
        attribute.addTransientModifier(
                new AttributeModifier(
                        attributeIdentifier,
                        damageReflection,
                        AttributeModifier.Operation.ADD_VALUE
                ));
    }

    @Override
    protected void onTick() {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

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
    protected void onEnd() {
        attribute.removeModifier(attributeIdentifier);
    }
}