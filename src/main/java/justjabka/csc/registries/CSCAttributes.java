package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class CSCAttributes {
    public static final Holder<Attribute> DODGE_CHANCE = register(
            "dodge_chance",
            0.0,
            0.0,
            1.0
    );

    public static final Holder<Attribute> DAMAGE_REFLECTION_PERCENT = register(
            "damage_reflection_percent",
            0.0,
            0.0,
            1.0
    );

    public static final Holder<Attribute> INCOMING_DAMAGE_MULTIPLIER = register(
            "incoming_damage_multiplier",
            1,
            0,
            1024
    );

    public static final Holder<Attribute> MAGIC_RESISTANCE = register(
            "magic_resistance",
            0,
            -1,
            1
    );

    public static final Holder<Attribute> MAGIC_DAMAGE = register(
            "magic_damage",
            1,
            0,
            1024
    );

    public static final Holder<Attribute> DAMAGE_BOOK_BONUS = register(
            "damage_book_bonus",
            0,
            0,
            1024
    );
    public static final Holder<Attribute> HEALTH_BOOK_BONUS = register(
            "health_book_bonus",
            0,
            0,
            1024
    );
    public static final Holder<Attribute> PHYSICAL_LIFE_STEAL = register(
            "physical_life_steal",
            0,
            0,
            1024
    );
    public static final Holder<Attribute> MAGICAL_LIFE_STEAL = register(
            "magical_life_steal",
            0,
            0,
            1024
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Attributes");
        syncAttributes();
    }

    public static void syncAttributes() {
        RangedAttribute attackDamage = (RangedAttribute) Attributes.ATTACK_DAMAGE.value();
        attackDamage.setSyncable(true);
    }

    private static Holder<Attribute> register(
            String name, double defaultValue, double minValue, double maxValue
    ) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CSC.MOD_ID, name);
        Attribute entityAttribute = new RangedAttribute(
                identifier.toLanguageKey("attribute.name"),
                defaultValue,
                minValue,
                maxValue
        ).setSyncable(true);

        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, identifier, entityAttribute);
    }
}