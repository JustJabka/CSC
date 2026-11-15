package justjabka.csc.contents.ability.shard;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.AbilityCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BerserkShardAbility extends Ability {
    public BerserkShardAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, -1024, AttributeModifier.Operation.ADD_VALUE);
    }
}
