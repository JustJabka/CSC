package justjabka.csc.handlers;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketsApi;
import justjabka.csc.registries.CSCItems;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class TrinketHandler {
    public static boolean hasTrinket(Player player, Item item, String slotId) {
        TrinketAttachment trinketAttachment = TrinketsApi.getAttachment(player);
        TrinketInventory inventory = trinketAttachment.getInventory(slotId);

        if (inventory == null) return false;

        Predicate<ItemStack> predicate = stack -> !stack.isEmpty() && item.equals(stack.getItem());
        return inventory.hasAnyMatching(predicate);
    }

    public static ItemStack findFirstTrinket(Player player, Item item, String slotId) {
        TrinketAttachment trinketAttachment = TrinketsApi.getAttachment(player);
        TrinketInventory inventory = trinketAttachment.getInventory(slotId);

        if (inventory == null) return ItemStack.EMPTY;

        for (ItemStack stack : inventory) {
            if (stack.isEmpty()) continue;
            if (!item.equals(stack.getItem())) continue;

            return stack;
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getFirstTrinket(Player player, String slotId) {
        TrinketAttachment trinketAttachment = TrinketsApi.getAttachment(player);

        TrinketInventory inventory = trinketAttachment.getInventory(slotId);
        if (inventory == null) return ItemStack.EMPTY;

        SlotAccess slot = inventory.getSlot(0);
        if (slot == null) return ItemStack.EMPTY;

        return slot.get();
    }

    public static boolean hasShard(Player player) {
        return TrinketHandler.hasTrinket(player, CSCItems.SHARD, "legs/belt");
    }
}
