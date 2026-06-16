package justjabka.csc.handlers;

import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

public class ShopHandler {
    public static List<Item> getItemsByCategory(ShopCategory category) {
        return BuiltInRegistries.ITEM.stream()
                .filter(item -> item instanceof ShopItem)
                .filter(item -> ((ShopItem) item).getCategory() == category)
                .toList();
    }
}
