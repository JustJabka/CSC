package justjabka.csc.contents.gui;

import justjabka.csc.handlers.ShopHandler;
import justjabka.csc.registries.CSCMenuTypes;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShopMenu extends AbstractContainerMenu {
    private static final int SLOTS_ROWS = 5;
    private static final int SLOTS_COLUMNS = 9;
    private static final int SLOTS_COUNT = SLOTS_ROWS * SLOTS_COLUMNS;

    private static final int CONTAINER_START = 0;
    private static final int CONTAINER_END = SLOTS_COUNT;
    private static final int INVENTORY_START = CONTAINER_END;
    private static final int INVENTORY_END = INVENTORY_START + Inventory.INVENTORY_SIZE;

    private static final int CONTAINER_START_X = 8;
    private static final int CONTAINER_START_Y = 18;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 122;

    private final Container container;

    // Стан вікна для конкретного гравця (за замовчуванням відкриваємо ФІЗ-урон)
    private ShopCategory currentCategory = ShopCategory.DAMAGE;
    private String searchQuery = "";

    public ShopMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT));
    }

    public ShopMenu(final int containerId, final Inventory inventory, final Container container) {
        super(CSCMenuTypes.SHOP_MENU, containerId);
        this.container = container;

        add5x9GridSlots();

        container.startOpen(inventory.player);

        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);

        this.refreshShopItems();
    }

    private void add5x9GridSlots() {
        for (int y = 0; y < SLOTS_ROWS; y++) {
            for (int x = 0; x < SLOTS_COLUMNS; x++) {
                final int slot = x + y * SLOTS_COLUMNS;
                this.addSlot(new Slot(
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

        for (int i = 0; i < SLOTS_COUNT; i++) {
            if (i < filteredItems.size()) {
                this.container.setItem(i, filteredItems.get(i));
            } else {
                this.container.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    public void changeCategory(ShopCategory newCategory) {
        this.currentCategory = newCategory;
        this.refreshShopItems();
    }

    public void changeSearchQuery(String query) {
        this.searchQuery = query;
        this.refreshShopItems();
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return ItemStack.EMPTY;
    }
}