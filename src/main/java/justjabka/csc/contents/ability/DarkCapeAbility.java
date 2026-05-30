package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class DarkCapeAbility extends BaseActiveAbility {
    public static final Identifier DARK_CAPE_ABILITY_KEY = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "dark_cape");

    public final float DAMAGE_MULTIPLIER = 2;
    private final AttributeModifier VULNERABILITY_MODIFIER = new AttributeModifier(
            key,
            0.02,
            AttributeModifier.Operation.ADD_VALUE
    );
    private final AttributeModifier SPEED_MODIFIER = new AttributeModifier(
            key,
            1.15,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    public DarkCapeAbility(int duration) {
        super(DARK_CAPE_ABILITY_KEY, duration);
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

        incomingDamageMultiplier.addTransientModifier(VULNERABILITY_MODIFIER);
        movementSpeed.addTransientModifier(SPEED_MODIFIER);
    }

    private void removeAttributes(Player player) {
        AttributeInstance incomingDamageMultiplier = player.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);
        AttributeInstance movementSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (incomingDamageMultiplier == null) return;
        if (movementSpeed == null) return;

        incomingDamageMultiplier.removeModifier(VULNERABILITY_MODIFIER);
        movementSpeed.removeModifier(SPEED_MODIFIER);
    }
}
