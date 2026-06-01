package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class DarkCapeAbility extends BaseActiveAbility {
    public final double damageMultiplier;
    public final AttributeModifier vulnerabilityModifier;
    public final AttributeModifier speedModifier;

    public DarkCapeAbility(Identifier key, int duration, double damageMultiplier, AttributeModifier vulnerabilityModifier, AttributeModifier speedModifier) {
        super(key, duration);
        this.damageMultiplier = damageMultiplier;
        this.vulnerabilityModifier = vulnerabilityModifier;
        this.speedModifier = speedModifier;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        player.setInvisible(true);
        addAttributes(player);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        Player player = ctx.player;

        player.setInvisible(false);
        removeAttributes(player);
    }

    private void addAttributes(Player player) {
        AttributeInstance incomingDamageMultiplier = player.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (incomingDamageMultiplier == null) return;
        if (movementSpeed == null) return;

        incomingDamageMultiplier.addTransientModifier(vulnerabilityModifier);
        movementSpeed.addTransientModifier(speedModifier);
    }

    private void removeAttributes(Player player) {
        AttributeInstance incomingDamageMultiplier = player.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (incomingDamageMultiplier == null) return;
        if (movementSpeed == null) return;

        incomingDamageMultiplier.removeModifier(vulnerabilityModifier);
        movementSpeed.removeModifier(speedModifier);
    }
}
