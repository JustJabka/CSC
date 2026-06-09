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

public class BerserkCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "berserk");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 18.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of();
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.BLOODY_LARYNX, 0
        );
    }
}
