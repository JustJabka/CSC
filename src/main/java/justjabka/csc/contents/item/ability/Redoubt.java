package justjabka.csc.contents.item.ability;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;

import java.util.Set;

public class Redoubt extends BaseActiveTrinketItem {
    public Redoubt(Properties properties) {
        super(properties.useCooldown(45)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.REDOUBT.getId(), TimeHandler.secondsToTicks(7), Set.of(ActivationType.TRINKET))
                )
        );
    }
}
