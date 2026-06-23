package justjabka.csc.contents.gui;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.component.UpgradableComponent;
import justjabka.csc.contents.item.book.SmithingBook;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCBlocks;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCMenuTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.Nullable;

public class UpgradeMenu extends AbstractContainerMenu {
    private static final Identifier EMPTY_SLOT_SMITHING_BOOK = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "container/slot/smithing_book");

    private static final int INPUT_SLOT = 0;
    private static final int ADDITIONAL_SLOT = 1;
    private static final int RESULT_SLOT = 2;

    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;

    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final Container inputSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            UpgradeMenu.this.slotsChanged(this);
        }
    };
    private final Container resultSlots = new ResultContainer();

    private final ContainerLevelAccess access;
    private final Player player;

    public UpgradeMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public UpgradeMenu(final int containerId, final Inventory inventory, final ContainerLevelAccess access) {
        super(CSCMenuTypes.UPGRADE_MENU, containerId);
        this.access = access;
        this.player = inventory.player;

        addSlots();
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }

    private void addSlots() {
        this.addSlot(new UpgradeInputSlot(this.inputSlots, INPUT_SLOT, 49, 19));
        this.addSlot(new UpgradeAdditionalSlot(this.inputSlots, ADDITIONAL_SLOT, 49, 40));
        this.addSlot(new UpgradeResultSlot(this.resultSlots, RESULT_SLOT, 129, 34, this));
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);

        if (container == this.inputSlots) {
            this.setupResultSlot();
        }
    }

    private void setupResultSlot() {
        ItemStack item = this.inputSlots.getItem(INPUT_SLOT);
        ItemStack smithingBook = this.inputSlots.getItem(ADDITIONAL_SLOT);

        if (!isValidInput(item, smithingBook)) return;

        ItemStack result = tryUpgradeItem(item, smithingBook);
        if (result == null) return;

        this.resultSlots.setItem(0, result);
        this.broadcastChanges();
    }

    private @Nullable ItemStack tryUpgradeItem(ItemStack item, ItemStack smithingBook) {
        UpgradableComponent upgradableComponent = item.get(CSCComponents.UPGRADABLE);
        if (upgradableComponent == null) return null;

        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        // Check Level Limit
        boolean isMaxLevel = upgradableComponent.level() >= upgradableComponent.maxLevel();

        if (isMaxLevel) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return null;
        }

        // Check Price
        boolean hasEnoughGold = data.gold() >= upgradableComponent.getPriceWithLevel();
        boolean hasSmithingBook = !smithingBook.isEmpty();

        if (!hasEnoughGold && !hasSmithingBook) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return null;
        }

        return upgradeItem(item, upgradableComponent);
    }

    private boolean isValidInput(ItemStack item, ItemStack smithingBook) {
        boolean isEmpty = item.isEmpty();
        boolean isValidAdditionalInput = smithingBook.isEmpty() || (smithingBook.getItem() instanceof SmithingBook);
        boolean isUpgradable = item.has(CSCComponents.UPGRADABLE);

        if (isEmpty || !isValidAdditionalInput || !isUpgradable) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return false;
        }

        return true;
    }

    private ItemStack upgradeItem(ItemStack item, UpgradableComponent upgradableComponent) {
        ItemStack result = item.copy();

        int additionalDurability = upgradableComponent.additionalDurability();

        // Update Level
        UpgradableComponent nextLevelComponent = upgradableComponent.getNextLevel();
        result.set(CSCComponents.UPGRADABLE, nextLevelComponent);

        // Update Durability
        if (additionalDurability > 0 && result.isDamageableItem()) {
            int newMaxDamage = result.getMaxDamage() + additionalDurability;
            result.set(DataComponents.MAX_DAMAGE, newMaxDamage);
        }

        // Update Enchantments
        upgradableComponent.additionalEnchantment().ifPresent(enchantmentHolder -> {
            ItemEnchantments enchants = result.getOrDefault(
                    DataComponents.ENCHANTMENTS,
                    ItemEnchantments.EMPTY
            );

            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(enchants);
            builder.set(enchantmentHolder, nextLevelComponent.level());

            result.set(DataComponents.ENCHANTMENTS, builder.toImmutable());
        });

        return result;
    }

    @Override
    public void removed(final Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.inputSlots));
    }

    @Override
    public boolean stillValid(final Player player) {
        return stillValid(this.access, player, CSCBlocks.UPGRADE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if (slot == null) return ItemStack.EMPTY;
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack item = slot.getItem();
        ItemStack itemCopy = item.copy();

        boolean isResultSlot = slotIndex == RESULT_SLOT;
        boolean isInputSlot  = slotIndex == INPUT_SLOT || slotIndex == ADDITIONAL_SLOT;
        boolean isPlayerInventory = slotIndex >= INV_SLOT_START && slotIndex < INV_SLOT_END;
        boolean isHotbar = slotIndex >= USE_ROW_SLOT_START && slotIndex < USE_ROW_SLOT_END;

        if (isResultSlot) {
            if (!this.moveItemStackTo(item, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(item, itemCopy);
        } else if (isInputSlot) {
            if (!this.moveItemStackTo(item, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(item, INPUT_SLOT, ADDITIONAL_SLOT + 1, false)) {
                if (isPlayerInventory) {
                    if (!this.moveItemStackTo(item, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isHotbar) {
                    if (!this.moveItemStackTo(item, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (item.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (item.getCount() == itemCopy.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, item);

        return itemCopy;
    }

    private static class UpgradeInputSlot extends Slot {
        public UpgradeInputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.has(CSCComponents.UPGRADABLE);
        }
    }

    private static class UpgradeAdditionalSlot extends Slot {
        public UpgradeAdditionalSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return (itemStack.getItem() instanceof SmithingBook);
        }

        @Override
        public Identifier getNoItemIcon() {
            return UpgradeMenu.EMPTY_SLOT_SMITHING_BOOK;
        }
    }

    private static class UpgradeResultSlot extends Slot {
        private final UpgradeMenu menu;

        public UpgradeResultSlot(Container container, int slot, int x, int y, UpgradeMenu menu) {
            super(container, slot, x, y);
            this.menu = menu;
        }

        @Override
        public boolean mayPlace(final ItemStack itemStack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            ItemStack oldItem = this.menu.inputSlots.getItem(INPUT_SLOT);
            ItemStack smithingBook = this.menu.inputSlots.getItem(ADDITIONAL_SLOT);

            UpgradableComponent upgradableComponent = oldItem.get(CSCComponents.UPGRADABLE);

            if (upgradableComponent != null && smithingBook.isEmpty()) {
                PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
                player.setAttached(
                        CSCAttachments.PLAYER_DATA,
                        data.removeGold(upgradableComponent.getPriceWithLevel())
                );
            }

            this.menu.inputSlots.removeItem(INPUT_SLOT, 1);
            this.menu.inputSlots.removeItem(ADDITIONAL_SLOT, 1);
        }
    }
}
