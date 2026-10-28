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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CSCItems {

    public static final List<Item> CREATIVE_TAB_ITEMS = new ArrayList<>();

    // Actual items
    public static final Item MIDAS = register(CSCItemIds.MIDAS, Midas::new);
    public static final Item PHOENIX_FEATHER = register(CSCItemIds.PHOENIX_FEATHER, PhoenixFeather::new);
    public static final Item THORNS = register(CSCItemIds.THORNS, Thorns::new);
    public static final Item MAGIC_PROTECTION_CLOCK = register(CSCItemIds.MAGIC_PROTECTION_CLOCK, MagicProtectionClock::new);
    public static final Item SHARD = register(CSCItemIds.SHARD, Shard::new);
    public static final Item DARK_GAUNTLET = register(CSCItemIds.DARK_GAUNTLET, DarkGauntlet::new);
    public static final Item DARK_CAPE = register(CSCItemIds.DARK_CAPE, DarkCape::new);
    public static final Item WILD_FOX_CAPE = register(CSCItemIds.WILD_FOX_CAPE, WildFoxCape::new);
    public static final Item VAMPIRIC_CAPE = register(CSCItemIds.VAMPIRIC_CAPE, VampiricCape::new);
    public static final Item HOLY_BLANKET = register(CSCItemIds.HOLY_BLANKET, HolyBlanket::new);
    public static final Item LIFE_SHIELD = register(CSCItemIds.LIFE_SHIELD, LifeShield::new);
    public static final Item STEEL_BOOTS = register(CSCItemIds.STEEL_BOOTS, SteelBoots::new);

    // Consumables
    public static final Item DAMAGE_BOOK = register(CSCItemIds.DAMAGE_BOOK, DamageBook::new);
    public static final Item HEALTH_BOOK = register(CSCItemIds.HEALTH_BOOK, HealthBook::new);
    public static final Item HEALTH_REGENERATION_BOOK = register(CSCItemIds.HEALTH_REGENERATION_BOOK, HealthRegenerationBook::new);
    public static final Item SMITHING_BOOK = register(CSCItemIds.SMITHING_BOOK, SmithingBook::new);
    public static final Item ASTRAL_DUST = register(CSCItemIds.ASTRAL_DUST, AstralDust::new);
    public static final Item HEALING_STEW = register(CSCItemIds.HEALING_STEW, HealingStew::new);

    // Character Abilities
    public static final Item SPINNING_SWORDS = register(CSCItemIds.SPINNING_SWORDS, SpinningSwords::new);
    public static final Item REDOUBT = register(CSCItemIds.REDOUBT, Redoubt::new);
    public static final Item BLOODY_LARYNX = register(CSCItemIds.BLOODY_LARYNX, BloodyLarynx::new);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Items");
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory) {
        return register(itemKey, itemFactory, new Item.Properties());
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        CREATIVE_TAB_ITEMS.add(item);

        return item;
    }
}