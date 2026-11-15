package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.Ability;
import justjabka.csc.contents.ability.BloodyLarynxAbility;
import justjabka.csc.contents.ability.RedoubtAbility;
import justjabka.csc.contents.ability.SpinningSwordsAbility;
import justjabka.csc.contents.ability.item.*;
import justjabka.csc.contents.ability.shard.BerserkShardAbility;
import justjabka.csc.types.AbilityCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

public class CSCAbilities {
    public static final Holder<Ability> EMPTY = register("empty", new Ability(AbilityCategory.NEUTRAL));

    // Generic Abilities
    public static final Holder<Ability> REDOUBT = register("redoubt", new RedoubtAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> SPINNING_SWORDS = register("spinning_swords", new SpinningSwordsAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> BLOODY_LARYNX = register("bloody_larynx", new BloodyLarynxAbility(AbilityCategory.BENEFICIAL));

    // Item Abilities
    public static final Holder<Ability> DARK_CAPE = register("dark_cape", new DarkCapeAbility(AbilityCategory.NEUTRAL));
    public static final Holder<Ability> DARK_GAUNTLET = register("dark_gauntlet", new DarkGauntletAbility(AbilityCategory.NEUTRAL));
    public static final Holder<Ability> HOLY_BLANKET = register("holy_blanket", new HolyBlanketAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> LIFE_SHIELD = register("life_shield", new LifeShieldAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> MAGIC_PROTECTION_CLOCK = register("magic_protection_clock", new MagicProtectionClockAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> STEEL_BOOTS = register("steel_boots", new SteelBootsAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> THORNS = register("thorns", new ThornsAbility(AbilityCategory.BENEFICIAL));

    public static final Holder<Ability> MIDAS = register("midas", new MidasAbility(AbilityCategory.BENEFICIAL));
    public static final Holder<Ability> PHOENIX_FEATHER = register("phoenix_feather", new PhoenixFeatherAbility(AbilityCategory.BENEFICIAL));

    // Shard Abilities
    public static final Holder<Ability> BERSERK_SHARD = register("berserk_shard", new BerserkShardAbility(AbilityCategory.BENEFICIAL));

    public static void initialize() {
        CSC.LOGGER.info("Initializing Abilities");
    }

    private static Holder<Ability> register(final String name, final Ability ability) {
        return Registry.registerForHolder(CSCBuiltInRegistries.ABILITY, CSC.id(name), ability);
    }
}
