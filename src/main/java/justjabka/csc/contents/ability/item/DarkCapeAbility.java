package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class DarkCapeAbility extends BaseActiveAbility {
    private final double DAMAGE_MULTIPLIER = 2;

    private static final double VULNERABILITY_MODIFIER = 0.02;
    private static final double SPEED_MODIFIER = 0.15;
    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                    getId(),
                    VULNERABILITY_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    getId(),
                    SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
    );

    public DarkCapeAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
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

    public double getDamageMultiplier() {
        return DAMAGE_MULTIPLIER;
    }

    private void addAttributes(Player player) {
        AttributeHandler.addTransientModifiers(player, ACTIVE_MODIFIERS);
    }

    private void removeAttributes(Player player) {
        AttributeHandler.removeModifiers(player, ACTIVE_MODIFIERS);
    }
}
