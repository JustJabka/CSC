package justjabka.csc.contents.item.generic;

import eu.pb4.trinkets.api.callback.TrinketCallback;

public abstract class BaseActiveTrinketItem extends BaseActiveItem implements TrinketCallback {
    public BaseActiveTrinketItem(Properties properties) {
        super(properties);
    }
}
