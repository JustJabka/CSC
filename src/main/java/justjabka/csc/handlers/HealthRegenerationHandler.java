package justjabka.csc.handlers;

import justjabka.csc.registries.CSCAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public final class HealthRegenerationHandler {

    private HealthRegenerationHandler() {}

    private static final int REGENERATION_PERIOD = 4 * 20;

    public static void tick(Player player) {
        if (player.tickCount % REGENERATION_PERIOD != 0) return;

        AttributeInstance regeneration = player.getAttribute(CSCAttributes.HEALTH_REGENERATION);
        if (regeneration == null) return;

        final float regenerationAmount = (float) regeneration.getValue();

        final boolean hasFullHealth = player.getHealth() == player.getMaxHealth();
        final boolean hasRegeneration = regenerationAmount > 0;
        if (hasFullHealth || !hasRegeneration) return;

        player.heal(regenerationAmount);
    }
}
