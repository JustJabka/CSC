package justjabka.csc.handlers;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ShopHandler {
    private static final Map<ShopCategory, List<ItemStack>> CATEGORY_CACHE = new EnumMap<>(ShopCategory.class);
    private static final List<ItemStack> ALL_SHOP_ITEMS = new ArrayList<>();

    public static void initCache() {
        ALL_SHOP_ITEMS.clear();
        for (ShopCategory cat : ShopCategory.values()) {
            CATEGORY_CACHE.put(cat, new ArrayList<>());
        }

        BuiltInRegistries.ITEM.stream().forEach(item -> {
            ItemStack defaultStack = item.getDefaultInstance();

            if (defaultStack.has(CSCComponents.SHOP_ITEM)) {
                ShopItemComponent component = defaultStack.get(CSCComponents.SHOP_ITEM);

                if (component == null) return;

                ALL_SHOP_ITEMS.add(defaultStack);
                CATEGORY_CACHE.get(component.category()).add(defaultStack);
            }
        });
    }

    public static List<ItemStack> getItemsByCategory(ShopCategory category) {
        return CATEGORY_CACHE.getOrDefault(category, List.of()).stream().map(ItemStack::copy).toList();
    }

    public static List<ItemStack> getAllItems() {
        return ALL_SHOP_ITEMS.stream().map(ItemStack::copy).toList();
    }

    public static void tryPurchase(Player player, ItemStack item) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        int currentGold = data.gold();
        ShopItemComponent shopItemComponent = item.getOrDefault(CSCComponents.SHOP_ITEM, new ShopItemComponent(0, ShopCategory.DAMAGE));
        int price = shopItemComponent.price();

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

        player.getInventory().add(item.copy());

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

    public static void trySell(Player player, ItemStack item) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        int usedItemPenalty = 2;
        ShopItemComponent shopItemComponent = item.getOrDefault(CSCComponents.SHOP_ITEM, new ShopItemComponent(0, ShopCategory.DAMAGE));
        int price = shopItemComponent.price() / usedItemPenalty;

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
