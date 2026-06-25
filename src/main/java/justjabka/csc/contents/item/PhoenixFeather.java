package justjabka.csc.contents.item;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.item.Rarity;

import java.util.Set;

public class PhoenixFeather extends BaseActiveTrinketItem {
    public PhoenixFeather(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON)
                .useCooldown(35)
                .component(CSCComponents.ABILITY, new AbilityComponent(CSCAbilities.PHOENIX_FEATHER.getId(), 0, Set.of(ActivationType.TRINKET)))
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(1500, ShopCategory.TACTIC))
        );
    }
}