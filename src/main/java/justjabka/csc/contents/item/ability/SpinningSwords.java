package justjabka.csc.contents.item.ability;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;

import java.util.Set;

public class SpinningSwords extends BaseActiveTrinketItem {
    public static final double DAMAGE_PERCENT = 0.08;
    public static final double DAMAGE_PERCENT_SHARD_BONUS = 0.02;

    public SpinningSwords(Properties properties) {
        super(properties.useCooldown(32)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.SPINNING_SWORDS.getId(), TimeHandler.secondsToTicks(5), Set.of(ActivationType.TRINKET))
                )
        );
    }
}
