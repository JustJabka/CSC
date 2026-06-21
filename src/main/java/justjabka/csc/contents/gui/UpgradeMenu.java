package justjabka.csc.contents.gui;

import justjabka.csc.registries.CSCMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;

    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final Container inputSlots = new SimpleContainer(2);
    private final Container resultSlots = new ResultContainer();

    private final Container container;

    public UpgradeMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(3));
    }

    public UpgradeMenu(final int containerId, final Inventory inventory, final Container container) {
        super(CSCMenuTypes.UPGRADE_MENU, containerId);
        this.container = container;

        addSlots();
        container.startOpen(inventory.player);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    private void addSlots() {
        this.addSlot(new Slot(this.inputSlots, INPUT_SLOT, 49, 19));
        this.addSlot(new Slot(this.inputSlots, ADDITIONAL_SLOT, 49, 40));
        this.addSlot(new Slot(this.resultSlots, RESULT_SLOT, 129, 34) {
            @Override
            public boolean mayPlace(final ItemStack itemStack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                UpgradeMenu.this.inputSlots.removeItem(INPUT_SLOT, 1);
                UpgradeMenu.this.inputSlots.removeItem(ADDITIONAL_SLOT, 1);
            }
        });
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        if (container == this.inputSlots) {
            this.setupResultSlot();
        }
    }

    private void setupResultSlot() {
        ItemStack weapon = this.inputSlots.getItem(INPUT_SLOT);
        ItemStack book = this.inputSlots.getItem(ADDITIONAL_SLOT);

        if (weapon.isEmpty() || book.isEmpty()) {
            this.resultSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
            return;
        }

        // TODO
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
