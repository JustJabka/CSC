package justjabka.csc.contents.character;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Map;

public class NoneCharacter extends Character {

    public NoneCharacter(Properties properties) {
        super(properties);
    }

    @Override
    public Identifier getShardAbility() {
        return null;
    }

    @Override
    public int getShardCooldown() {
        return 0;
    }

    @Override
    public int getShardDuration() {
        return 0;
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of();
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of();
    }
}
