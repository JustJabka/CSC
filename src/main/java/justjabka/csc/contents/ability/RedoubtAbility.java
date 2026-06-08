package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;

public class RedoubtAbility extends BaseActiveAbility {
    private final Map<Holder<Attribute>, AttributeModifier> activeModifiers;

    public RedoubtAbility(Identifier key, int duration, double incomingDamageMultiplierModifier, double knockbackResistanceModifier) {
        super(key, duration);
        this.activeModifiers = Map.of(
                CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                        key,
                        incomingDamageMultiplierModifier,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                        key,
                        knockbackResistanceModifier,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }

    @Override
    public void onStart() {
        AttributeHandler.addTransientModifiers(ctx.player, activeModifiers);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        AttributeHandler.removeModifiers(ctx.player, activeModifiers);
    }
}
