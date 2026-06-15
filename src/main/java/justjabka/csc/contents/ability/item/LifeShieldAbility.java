package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class LifeShieldAbility extends BaseActiveAbility {
    private final Map<Holder<Attribute>, AttributeModifier> activeModifiers;
    private final float healAmount;
    private final float absorptionAmount;

    public LifeShieldAbility(Identifier key, int duration, Map<Holder<Attribute>, AttributeModifier> activeModifiers, float healAmount, float absorptionAmount) {
        super(key, duration);
        this.activeModifiers = activeModifiers;
        this.healAmount = healAmount;
        this.absorptionAmount = absorptionAmount;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifiers(player, activeModifiers);

        player.heal(healAmount);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + absorptionAmount);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        AttributeHandler.removeModifiers(ctx.player, activeModifiers);
    }
}
