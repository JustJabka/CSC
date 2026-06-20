package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class CSCLootTables {
    public static ResourceKey<LootTable> ITEM_SELECTION_LOOT = create("item_selection/all_items");;

    private static ResourceKey<LootTable> create(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(CSC.MOD_ID, path));
    }

    public static void initialize() {
        CSC.LOGGER.info("Initializing Loot Tables");
    }
}
