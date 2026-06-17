package justjabka.csc.handlers;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShopHandler {
    public static List<Item> getItemsByCategory(ShopCategory category) {
        return BuiltInRegistries.ITEM.stream()
                .filter(item -> item instanceof ShopItem)
                .filter(item -> ((ShopItem) item).getCategory() == category)
                .toList();
    }

    public static void tryPurchase(Player player, ShopItem shopItem, Item item) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        int currentGold = data.gold();
        int price = shopItem.getPrice();

        boolean notEnoughGold = currentGold < price;
        boolean inventoryFull = player.getInventory().getFreeSlot() == -1;

        if (notEnoughGold || inventoryFull) {
            player.level().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    CSCSounds.UI_SHOP_PURCHASE_FAIL,
                    SoundSource.UI,
                    0.5f,
                    1
            );
            return;
        }

        ItemStack purchasedStack = new ItemStack(item);
        player.getInventory().add(purchasedStack);

        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.removeGold(price)
        );

        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                CSCSounds.UI_SHOP_PURCHASE_SUCCESS,
                SoundSource.UI,
                1,
                1
        );
    }

    public static void trySell(Player player, ShopItem shopItem, ItemStack item) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        int usedItemPenalty = 2;
        int price = shopItem.getPrice() / usedItemPenalty;

        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.addGold(price)
        );

        item.shrink(1);

        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                CSCSounds.UI_SHOP_PURCHASE_SUCCESS,
                SoundSource.UI,
                1,
                1
        );
    }
}
