package justjabka.csc.contents.character.generic;

import justjabka.csc.CSC;
import justjabka.csc.handlers.AttributeHandler;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public abstract class BaseCharacter {
    public abstract Identifier getKey();
    public abstract Identifier getDisplayIcon();

    public abstract Map<Holder<Attribute>, Double> getBaseAttributes();
    public abstract Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers();

    public Component getDisplayName() {
        String key = "character.%s".formatted(getKey());
        return Component.translatableWithFallback(key, "Sorry the translate broke :(");
    }

    public void setBaseAttributes(Player player) {
        AttributeHandler.resetBaseValues(player);
        AttributeHandler.setBaseValues(player, getBaseAttributes());
    }

    public void applyAttributeModifiers(Player player) {
        Map<Holder<Attribute>, AttributeModifier> modifiers = getAttributeModifiers();

        AttributeHandler.removeAllModifiersFromNamespace(player, CSC.MOD_ID);
        AttributeHandler.addTransientModifiers(player, modifiers);
    }

    public void onSelect(Player player) {
        setBaseAttributes(player);
        applyAttributeModifiers(player);
    }
}
