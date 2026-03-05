package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class ThornsAbility extends ActiveAbility {
    private final double damageReflection;
    AttributeInstance attribute = player.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);
    Identifier ABILITY_DAMAGE_REFLECTION_PERCENT_ID = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "thorns_ability");

    public ThornsAbility(Player player, InteractionHand hand, int durationTicks, double damageReflection) {
        super(player, hand, durationTicks);
        this.togglable = false;
        this.damageReflection = damageReflection;
    }

    @Override
    public void onStart() {
        attribute.addTransientModifier(
                new AttributeModifier(
                        ABILITY_DAMAGE_REFLECTION_PERCENT_ID,
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
    public void onEnd() {
        attribute.removeModifier(ABILITY_DAMAGE_REFLECTION_PERCENT_ID);
    }
}