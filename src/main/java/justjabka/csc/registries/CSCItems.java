package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.item.Midas;
import justjabka.csc.contents.item.PhoenixFeather;
import justjabka.csc.contents.item.Thorns;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CSCItems {
    public static final List<Item> MOD_ITEMS = new ArrayList<>();

    public static final Item MIDAS = register("midas", Midas::new, new Item.Properties());
    public static final Item PHOENIX_FEATHER = register("phoenix_feather", PhoenixFeather::new, new Item.Properties());
    public static final Item THORNS = register("thorns", Thorns::new, new Item.Properties());

    public static void initialize() {
        CSC.LOGGER.info("Initializing Items");
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CSC.MOD_ID, name));

        // Create the item instance.
        GenericItem item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        MOD_ITEMS.add(item);

        return item;
    }
}