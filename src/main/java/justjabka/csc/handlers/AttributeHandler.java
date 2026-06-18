package justjabka.csc.handlers;

import justjabka.csc.CSC;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class AttributeHandler {
    public static final Identifier BASE_MAX_HEALTH_ID = Identifier.withDefaultNamespace("base_max_health");

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

    public static void removeAllModifiersFromNamespace(Player player, String namespace) {
        Collection<AttributeInstance> attributes = player.getAttributes().getSyncableAttributes();

        for (AttributeInstance instance : attributes) {
            if (instance == null) continue;

            Set<AttributeModifier> toRemove = new HashSet<>();

            for (AttributeModifier modifier : instance.getModifiers()) {
                String modifierNamespace = modifier.id().getNamespace();

                if (!modifierNamespace.equals(namespace)) continue;

                toRemove.add(modifier);
            }

            for (AttributeModifier modifier : toRemove) {
                instance.removeModifier(modifier);
            }
        }
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

    public static void addTrinketModifier(double value, Holder<Attribute> attribute, AttributeModifier.Operation operation, Identifier key, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        String attributeName = attribute.getRegisteredName().replace(":", "_");

        AttributeModifier modifier = new AttributeModifier(
                key.withSuffix("%s/%s".formatted(CSC.MOD_ID, attributeName)),
                value,
                operation
        );

        consumer.accept(attribute, modifier);
    }
}
