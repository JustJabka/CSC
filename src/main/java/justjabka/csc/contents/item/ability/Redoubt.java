package justjabka.csc.contents.item.ability;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;

public class Redoubt extends BaseActiveTrinketItem {
    private static final double INCOMING_DAMAGE_MULTIPLIER_MODIFIER = -0.35;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = 1;

    public Redoubt(Properties properties) {
        super(properties.useCooldown(45)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.REDOUBT.getId(), TimeHandler.secondsToTicks(7))
                )
        );
    }
}
