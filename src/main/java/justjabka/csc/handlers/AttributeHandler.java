package justjabka.csc.handlers;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class AttributeHandler {
    public static void addTransientModifier(Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;
        if (instance.hasModifier(modifier.id())) return;

        instance.addTransientModifier(modifier);
    }

    public static void removeModifier(Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.removeModifier(modifier);
    }

    public static void addTransientModifiers(Player player, Map<Holder<Attribute>, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, modifier) ->
                addTransientModifier(player, attribute, modifier));
    }

    public static void removeModifiers(Player player, Map<Holder<Attribute>, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, modifier) ->
                removeModifier(player, attribute, modifier));
    }

    public static void setBaseValues(Player player, Map<Holder<Attribute>, Double> attributes) {
        attributes.forEach((attribute, value) ->
                setBaseValue(player, attribute, value));
    }

    public static void setBaseValue(Player player, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.setBaseValue(value);
    }

    public static void resetBaseValues(Player player) {
        player.getAttributes().getSyncableAttributes().forEach(instance -> {
            if (instance == null) return;

            player.getAttributes().resetBaseValue(instance.getAttribute());
        });
    }
}
