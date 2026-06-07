package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.registries.CSCItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.Map;

public class SwordsmanCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "swordsman");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 22.0,
                Attributes.ATTACK_DAMAGE, 1.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of(
                Attributes.ATTACK_DAMAGE, new AttributeModifier(getKey(), 0.16, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        );
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.SPINNING_SWORDS, 0
        );
    }
}
