package justjabka.csc.contents.item.ability;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;

import java.util.Set;

public class BloodyLarynx extends BaseActiveTrinketItem {
    public BloodyLarynx(Properties properties) {
        super(properties.useCooldown(60)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.BLOODY_LARYNX.getId(), 15, Set.of(ActivationType.TRINKET, ActivationType.PASSIVE))
                )
        );
    }
}
