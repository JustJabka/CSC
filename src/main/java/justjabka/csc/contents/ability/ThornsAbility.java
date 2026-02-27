package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.handlers.TimedAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ThornsAbility extends TimedAbility {
    private final double damageReflection;

    public ThornsAbility(Player player, int durationTicks, double damageReflection) {
        super(player, durationTicks);
        this.damageReflection = damageReflection;
    }

    @Override
    protected void onStart() {
        CSC.LOGGER.info("Thorns Start");
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
        CSC.LOGGER.info("Thorns End");
    }
}