package justjabka.csc.handlers;

import justjabka.csc.contents.gui.ItemSelectionMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemSelectionHandler {
    private static final List<ItemStack> DEFAULT_LIST = Collections.nCopies(8, ItemStack.EMPTY);
    private static final Component ITEM_SELECTION_NAME = Component.translatable("container.item_selection");

    public static List<ItemStack> generateOptions(ServerPlayer player) {
        List<ItemStack> generatedItems = new ArrayList<>();
        ServerLevel level = player.level();

        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(LootContextParamSets.COMMAND);

        MinecraftServer server = level.getServer();
        if (server == null) return DEFAULT_LIST;

        for (int i = 0; i < 4; i++) {
            rollLootTable(BuiltInLootTables.END_CITY_TREASURE, lootParams, generatedItems, server);
            rollLootTable(BuiltInLootTables.NETHER_BRIDGE, lootParams, generatedItems, server);
        }

        return generatedItems;
    }

    public static void openMenu(ServerPlayer player) {
        List<ItemStack> options = generateOptions(player);
        SimpleContainer guiContainer = new SimpleContainer(8);

        for (int i = 0; i < options.size(); i++) {
            guiContainer.setItem(i, options.get(i));
        }

        player.openMenu(
                new SimpleMenuProvider(
                (containerId, inventory, p) -> new ItemSelectionMenu(containerId, inventory, guiContainer),
                ITEM_SELECTION_NAME
        ));
    }

    private static void rollLootTable(ResourceKey<LootTable> key, LootParams lootParams, List<ItemStack> generatedItems, MinecraftServer server) {
        LootTable lootTable = server.reloadableRegistries().getLootTable(key);
        List<ItemStack> rolledItem = lootTable.getRandomItems(lootParams);

        if (!rolledItem.isEmpty()) {
            generatedItems.add(rolledItem.getFirst());
        } else {
            generatedItems.add(ItemStack.EMPTY);
        }
    }
}
