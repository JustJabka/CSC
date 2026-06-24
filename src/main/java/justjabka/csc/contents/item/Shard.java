package justjabka.csc.contents.item;

import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;

public class Shard extends BaseActiveTrinketItem {
    public Shard(Properties properties) {
        super(properties.component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2300, ShopCategory.MAGIC)));
    }
}
