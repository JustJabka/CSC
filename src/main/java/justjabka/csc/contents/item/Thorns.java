package justjabka.csc.contents.item;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.item.Rarity;

import java.util.Set;

public class Thorns extends BaseActiveTrinketItem {
    public Thorns(Properties properties) {
        super(properties.rarity(Rarity.RARE)
                .useCooldown(45)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.THORNS.getId(), TimeHandler.secondsToTicks(5), Set.of(ActivationType.TRINKET))
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2800, ShopCategory.MAGIC))
        );
    }
}