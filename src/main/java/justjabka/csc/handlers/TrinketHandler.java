package justjabka.csc.handlers;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketsApi;
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
}
