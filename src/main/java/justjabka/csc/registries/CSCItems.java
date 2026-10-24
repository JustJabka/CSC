package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.item.*;
import justjabka.csc.contents.item.ability.BloodyLarynx;
import justjabka.csc.contents.item.ability.Redoubt;
import justjabka.csc.contents.item.ability.SpinningSwords;
import justjabka.csc.contents.item.book.DamageBook;
import justjabka.csc.contents.item.book.HealthBook;
import justjabka.csc.contents.item.book.HealthRegenerationBook;
import justjabka.csc.contents.item.book.SmithingBook;
import justjabka.csc.contents.item.consumable.AstralDust;
import justjabka.csc.contents.item.consumable.HealingStew;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CSCItems {
    public static final List<Item> CREATIVE_TAB_ITEMS = new ArrayList<>();

    // Actual items
    public static final Item MIDAS = register("midas", Midas::new);
    public static final Item PHOENIX_FEATHER = register("phoenix_feather", PhoenixFeather::new);
    public static final Item THORNS = register("thorns", Thorns::new);
    public static final Item MAGIC_PROTECTION_CLOCK = register("magic_protection_clock", MagicProtectionClock::new);
    public static final Item SHARD = register("shard", Shard::new);
    public static final Item DARK_GAUNTLET = register("dark_gauntlet", DarkGauntlet::new);
    public static final Item DARK_CAPE = register("dark_cape", DarkCape::new);
    public static final Item WILD_FOX_CAPE = register("wild_fox_cape", WildFoxCape::new);
    public static final Item VAMPIRIC_CAPE = register("vampiric_cape", VampiricCape::new);
    public static final Item HOLY_BLANKET = register("holy_blanket", HolyBlanket::new);
    public static final Item LIFE_SHIELD = register("life_shield", LifeShield::new);
    public static final Item STEEL_BOOTS = register("steel_boots", SteelBoots::new);

    // Consumables
    public static final Item DAMAGE_BOOK = register("damage_book", DamageBook::new);
    public static final Item HEALTH_BOOK = register("health_book", HealthBook::new);
    public static final Item HEALTH_REGENERATION_BOOK = register("health_regeneration_book", HealthRegenerationBook::new);
    public static final Item SMITHING_BOOK = register("smithing_book", SmithingBook::new);
    public static final Item ASTRAL_DUST = register("astral_dust", AstralDust::new);
    public static final Item HEALING_STEW = register("healing_stew", HealingStew::new);

    // Character Abilities
    public static final Item SPINNING_SWORDS = register("spinning_swords", SpinningSwords::new);
    public static final Item REDOUBT = register("redoubt", Redoubt::new);
    public static final Item BLOODY_LARYNX = register("bloody_larynx", BloodyLarynx::new);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Items");
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory) {
        return register(name, itemFactory, new Item.Properties());
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CSC.MOD_ID, name));

        // Create the item instance.
        GenericItem item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        CREATIVE_TAB_ITEMS.add(item);

        return item;
    }
}