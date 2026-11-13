package justjabka.csc.handlers;

import justjabka.csc.CSC;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public final class AttributeHandler {

    private AttributeHandler() {}

    public static final Identifier BASE_MAX_HEALTH_ID = Identifier.withDefaultNamespace("base_max_health");
    public static final Identifier BASE_ATTACK_DAMAGE_ID = CSC.id("base_attack_damage");
    public static final Identifier BASE_MAGIC_DAMAGE_ID = Identifier.withDefaultNamespace("base_magic_damage");

    public static void addTransientModifier(Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;
        if (instance.hasModifier(modifier.id())) return;

        instance.addTransientModifier(modifier);
    }

    public static void addPermanentModifier(Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;
        if (instance.hasModifier(modifier.id())) return;

        instance.addPermanentModifier(modifier);
    }

    public static void removeModifier(Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.removeModifier(modifier);
    }

    public static void removeModifier(Player player, Holder<Attribute> attribute, Identifier modifier) {
        AttributeInstance instance = player.getAttribute(attribute);

        if (instance == null) return;

        instance.removeModifier(modifier);
    }

    public static void addTransientModifiers(Player player, Map<Holder<Attribute>, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, modifier) ->
                addTransientModifier(player, attribute, modifier));
    }

    public static void addPermanentModifiers(Player player, Map<Holder<Attribute>, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, modifier) ->
                addPermanentModifier(player, attribute, modifier));
    }

    public static void removeModifiers(Player player, Map<Holder<Attribute>, AttributeModifier> modifiers) {
        modifiers.forEach((attribute, modifier) ->
                removeModifier(player, attribute, modifier));
    }

    public static void removeAllModifiersFrom(Player player, String namespace, @Nullable String prefix) {
        Collection<AttributeInstance> attributes = player.getAttributes().getSyncableAttributes();

        for (AttributeInstance instance : attributes) {
            if (instance == null) continue;

            Set<AttributeModifier> toRemove = new HashSet<>();

            for (AttributeModifier modifier : instance.getModifiers()) {
                Identifier id = modifier.id();

                String modifierNamespace = id.getNamespace();
                String modifierPath = id.getPath();

                if (!modifierNamespace.equals(namespace)) continue;
                if (prefix != null && !modifierPath.startsWith(prefix)) continue;

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
