package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class DarkCapeAbility extends BaseActiveAbility {
    public final double damageMultiplier;
    public final Map<Holder<Attribute>, AttributeModifier> activeModifiers;

    public DarkCapeAbility(Identifier key, int duration, double damageMultiplier, Map<Holder<Attribute>, AttributeModifier> activeModifiers) {
        super(key, duration);
        this.damageMultiplier = damageMultiplier;
        this.activeModifiers = activeModifiers;
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
        AttributeHandler.addTransientModifiers(player, activeModifiers);
    }

    private void removeAttributes(Player player) {
        AttributeHandler.removeModifiers(player, activeModifiers);
    }
}
