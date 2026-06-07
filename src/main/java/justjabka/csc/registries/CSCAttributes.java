package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CSCAttributes {
    public static final Holder<Attribute> DODGE_CHANCE = register(
            "dodge_chance",
            0.0,
            0.0,
            1.0,
            true
    );

    public static final Holder<Attribute> DAMAGE_REFLECTION_PERCENT = register(
            "damage_reflection_percent",
            0.0,
            0.0,
            1.0,
            true
    );

    public static final Holder<Attribute> INCOMING_DAMAGE_MULTIPLIER = register(
            "incoming_damage_multiplier",
            1,
            0,
            1024,
            true
    );

    public static final Holder<Attribute> MAGIC_RESISTANCE = register(
            "magic_resistance",
            0,
            -1,
            1,
            true
    );

    public static final Holder<Attribute> MAGIC_DAMAGE = register(
            "magic_damage",
            1,
            0,
            1024,
            true
    );

    private static Holder<Attribute> register(
            String name, double defaultValue, double minValue, double maxValue, boolean syncedWithClient
    ) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CSC.MOD_ID, name);
        Attribute entityAttribute = new RangedAttribute(
                identifier.toLanguageKey(),
                defaultValue,
                minValue,
                maxValue
        ).setSyncable(syncedWithClient);

        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, identifier, entityAttribute);
    }

    public static void initialize() {
        CSC.LOGGER.info("Initializing Attributes");
    }
}