package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.RedoubtAbility;
import justjabka.csc.contents.ability.SpinningSwordsAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.item.*;
import justjabka.csc.contents.ability.shard.BerserkShardAbility;
import justjabka.csc.types.AbilityType;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CSCAbilities {
    private static final Map<Identifier, AbilityType<?>> ABILITY_TYPES = new HashMap<>();

    // Generic Abilities
    public static final AbilityType<RedoubtAbility> REDOUBT = register("redoubt", RedoubtAbility::new);
    public static final AbilityType<SpinningSwordsAbility> SPINNING_SWORDS = register("spinning_swords", SpinningSwordsAbility::new);

    // Item Abilities
    public static final AbilityType<DarkCapeAbility> DARK_CAPE = register("dark_cape", DarkCapeAbility::new);
    public static final AbilityType<DarkGauntletAbility> DARK_GAUNTLET = register("dark_gauntlet", DarkGauntletAbility::new);
    public static final AbilityType<HolyBlanketAbility> HOLY_BLANKET = register("holy_blanket", HolyBlanketAbility::new);
    public static final AbilityType<LifeShieldAbility> LIFE_SHIELD = register("life_shield", LifeShieldAbility::new);
    public static final AbilityType<MagicProtectionClockAbility> MAGIC_PROTECTION_CLOCK = register("magic_protection_clock", MagicProtectionClockAbility::new);
    public static final AbilityType<SteelBootsAbility> STEEL_BOOTS = register("steel_boots", SteelBootsAbility::new);
    public static final AbilityType<ThornsAbility> THORNS = register("thorns", ThornsAbility::new);

    // Shard Abilities
    public static final AbilityType<BerserkShardAbility> BERSERK_SHARD = register("berserk_shard", BerserkShardAbility::new);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Abilities");
    }

    public static <T extends BaseActiveAbility> AbilityType<T> register(
            String name,
            AbilityType.AbilityFactory<T> factory
    ) {
        Identifier key = Identifier.fromNamespaceAndPath(CSC.MOD_ID, name);
        AbilityType<T> type = new AbilityType<>(key, factory);

        ABILITY_TYPES.put(key, type);
        return type;
    }

    public static AbilityType<?> getByKey(Identifier key) {
        return ABILITY_TYPES.get(key);
    }

    public static Map<Identifier, AbilityType<?>> getAbilityTypes() {
        return Collections.unmodifiableMap(ABILITY_TYPES);
    }
}
