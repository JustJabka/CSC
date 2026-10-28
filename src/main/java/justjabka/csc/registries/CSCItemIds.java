package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class CSCItemIds {

    // Actual items
    public static final ResourceKey<Item> MIDAS = create("midas");
    public static final ResourceKey<Item> PHOENIX_FEATHER = create("phoenix_feather");
    public static final ResourceKey<Item> THORNS = create("thorns");
    public static final ResourceKey<Item> MAGIC_PROTECTION_CLOCK = create("magic_protection_clock");
    public static final ResourceKey<Item> SHARD = create("shard");
    public static final ResourceKey<Item> DARK_GAUNTLET = create("dark_gauntlet");
    public static final ResourceKey<Item> DARK_CAPE = create("dark_cape");
    public static final ResourceKey<Item> WILD_FOX_CAPE = create("wild_fox_cape");
    public static final ResourceKey<Item> VAMPIRIC_CAPE = create("vampiric_cape");
    public static final ResourceKey<Item> HOLY_BLANKET = create("holy_blanket");
    public static final ResourceKey<Item> LIFE_SHIELD = create("life_shield");
    public static final ResourceKey<Item> STEEL_BOOTS = create("steel_boots");

    // Consumables
    public static final ResourceKey<Item> DAMAGE_BOOK = create("damage_book");
    public static final ResourceKey<Item> HEALTH_BOOK = create("health_book");
    public static final ResourceKey<Item> HEALTH_REGENERATION_BOOK = create("health_regeneration_book");
    public static final ResourceKey<Item> SMITHING_BOOK = create("smithing_book");
    public static final ResourceKey<Item> ASTRAL_DUST = create("astral_dust");
    public static final ResourceKey<Item> HEALING_STEW = create("healing_stew");

    // Character Abilities
    public static final ResourceKey<Item> SPINNING_SWORDS = create("spinning_swords");
    public static final ResourceKey<Item> REDOUBT = create("redoubt");
    public static final ResourceKey<Item> BLOODY_LARYNX = create("bloody_larynx");

    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        return ResourceKey.create(Registries.ITEM, CSC.id(name));
    }
}
