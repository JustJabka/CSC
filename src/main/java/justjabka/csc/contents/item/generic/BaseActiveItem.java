package justjabka.csc.contents.item.generic;

import net.minecraft.world.item.Item;

public abstract class BaseActiveItem extends Item {
    public BaseActiveItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}