package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.RedoubtAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import net.minecraft.resources.Identifier;

public class Redoubt extends BaseActiveTrinketItem {
    private static final double INCOMING_DAMAGE_MULTIPLIER_MODIFIER = -0.35;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = 1;

    public Redoubt(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "redoubt");
    }

    @Override
    protected int getCooldown() {
        return 45;
    }

    @Override
    protected int getDuration() {
        return 7;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return new RedoubtAbility(
                getKey(),
                getSecondsToTicks(getDuration()),
                INCOMING_DAMAGE_MULTIPLIER_MODIFIER,
                KNOCKBACK_RESISTANCE_MODIFIER
        );
    }
}
