package justjabka.csc.contents.gui;

import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.handlers.ShopHandler;
import justjabka.csc.registries.CSCMenuTypes;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ShopMenu extends AbstractContainerMenu {
    private static final int SLOTS_ROWS = 3;
    private static final int SLOTS_COLUMNS = 9;
    private static final int VISIBLE_SLOTS = SLOTS_ROWS * SLOTS_COLUMNS;

    private static final int CONTAINER_START = 0;
    private static final int CONTAINER_END = VISIBLE_SLOTS;
    private static final int INVENTORY_START = CONTAINER_END;
    private static final int INVENTORY_END = INVENTORY_START + Inventory.INVENTORY_SIZE;

    private static final int CONTAINER_START_X = 8;
    private static final int CONTAINER_START_Y = 18;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 86;

    private final Container container;
    private ShopCategory currentCategory = ShopCategory.DAMAGE;
    private String searchQuery = "";

    public ShopMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(VISIBLE_SLOTS));
    }

    public ShopMenu(final int containerId, final Inventory inventory, final Container container) {
        super(CSCMenuTypes.SHOP_MENU, containerId);
        this.container = container;

        add5x9GridSlots();
        container.startOpen(inventory.player);
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);

        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return currentCategory.ordinal();
            }

            @Override
            public void set(int value) {
                if (value >= 0 && value < ShopCategory.values().length) {
                    currentCategory = ShopCategory.values()[value];
                }
            }
        });

        this.refreshShopItems();
    }

    private void add5x9GridSlots() {
        for (int y = 0; y < SLOTS_ROWS; y++) {
            for (int x = 0; x < SLOTS_COLUMNS; x++) {
                final int slot = x + y * SLOTS_COLUMNS;
                this.addSlot(new ShopSlot(
                        this.container,
                        slot,
                        CONTAINER_START_X + x * SLOT_SIZE,
                        CONTAINER_START_Y + y * SLOT_SIZE
                ));
            }
        }
    }

    public void refreshShopItems() {
        List<ItemStack> filteredItems = ShopHandler
                .getItemsByCategory(currentCategory).stream()
                .filter(shopItem -> {
                    if (searchQuery.isEmpty()) return true;
                    String name = shopItem.getDefaultInstance().getDisplayName().getString().toLowerCase();
                    return name.contains(searchQuery.toLowerCase());
                })
                .map(ItemStack::new)
                .toList();

        for (int i = 0; i < VISIBLE_SLOTS; i++) {
            if (i < filteredItems.size()) {
                this.container.setItem(i, filteredItems.get(i));
                continue;
            }

            this.container.setItem(i, ItemStack.EMPTY);
        }
    }

    public void changeCategory(ShopCategory newCategory) {
        this.currentCategory = newCategory;
        this.refreshShopItems();
        this.broadcastChanges();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < ShopCategory.values().length) {
            this.changeCategory(ShopCategory.values()[id]);
            return true;
        }
        return false;
    }

    public void changeSearchQuery(String query) {
        this.searchQuery = query;
        this.refreshShopItems();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (!slotInInventory(slotIndex)) return ItemStack.EMPTY;
        
        if (player.level().isClientSide()) return ItemStack.EMPTY;

        ItemStack clickedStack = getClickedStack(slotIndex);
        if (clickedStack == null) return ItemStack.EMPTY;

        Item clickedItem = clickedStack.getItem();
        if (!(clickedItem instanceof ShopItem shopItem)) return ItemStack.EMPTY;

        ItemStack clickedStackCopy = clickedStack.copy();

        ShopHandler.trySell(player, shopItem, clickedStack);
        this.sendAllDataToRemote();

        return clickedStackCopy;
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        if (slotInInventory(slotIndex)) {
            super.clicked(slotIndex, buttonNum, containerInput, player);
            return;
        }

        if (containerInput != ContainerInput.PICKUP) return;
        if (player.level().isClientSide()) return;

        ItemStack clickedStack = getClickedStack(slotIndex);
        if (clickedStack == null) return;

        Item clickedItem = clickedStack.getItem();
        if (!(clickedItem instanceof ShopItem shopItem)) return;

        ShopHandler.tryPurchase(player, shopItem, clickedItem);
        this.sendAllDataToRemote();
    }

    private @Nullable ItemStack getClickedStack(int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) return null;

        return slot.getItem();
    }

    private static boolean slotInInventory(int slotIndex) {
        return slotIndex < CONTAINER_START || slotIndex >= CONTAINER_END;
    }

    public ShopCategory getCurrentCategory() {
        return currentCategory;
    }

    private static class ShopSlot extends Slot {
        public ShopSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPickup(net.minecraft.world.entity.player.Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}