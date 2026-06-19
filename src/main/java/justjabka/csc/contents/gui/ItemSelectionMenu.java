package justjabka.csc.contents.gui;

import justjabka.csc.registries.CSCMenuTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Range;

public class ItemSelectionMenu extends AbstractContainerMenu {
    private static final int SLOTS_ROWS = 2;
    private static final int SLOTS_COLUMNS = 4;
    private static final int SLOTS_OFFSET = 1;
    private static final int VISIBLE_SLOTS = SLOTS_ROWS * SLOTS_COLUMNS;

    private static final int CONTAINER_START = 0;
    private static final int CONTAINER_END = VISIBLE_SLOTS;
    private static final int INVENTORY_START = CONTAINER_END;
    private static final int INVENTORY_END = INVENTORY_START + Inventory.INVENTORY_SIZE;

    private static final int CONTAINER_START_X = 26;
    private static final int CONTAINER_START_Y = 20;
    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 69;

    private final Container container;

    public ItemSelectionMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(VISIBLE_SLOTS));
    }

    public ItemSelectionMenu(final int containerId, final Inventory inventory, final Container container) {
        super(CSCMenuTypes.ITEM_SELECTION_MENU, containerId);
        this.container = container;

        add2x4GridSlots();
        container.startOpen(inventory.player);
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    private void add2x4GridSlots() {
        for (int y = 0; y < SLOTS_ROWS; y++) {
            for (int x = 0; x < SLOTS_COLUMNS; x++) {
                final int slot = x + y * SLOTS_COLUMNS;
                this.addSlot(new ItemSelectionSlot(
                        this.container,
                        slot,
                        CONTAINER_START_X + x * SLOT_SIZE * 2,
                        CONTAINER_START_Y + y * SLOT_SIZE
                ));
            }
        }
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        Range<Integer> inventorySlots = Range.of(INVENTORY_START, INVENTORY_END);

        if (inventorySlots.contains(slotIndex)) {
            super.clicked(slotIndex, buttonNum, containerInput, player);
            return;
        }

        if (containerInput != ContainerInput.PICKUP) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) return;

        ItemStack clickedStack = this.container.removeItem(slot.getContainerSlot(), slot.getItem().getCount());

        if (!clickedStack.isEmpty()) {
            serverPlayer.addItem(clickedStack);
        }

        this.container.clearContent();
        serverPlayer.closeContainer();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    private static class ItemSelectionSlot extends Slot {
        public ItemSelectionSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
