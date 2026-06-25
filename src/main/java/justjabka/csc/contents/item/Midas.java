package justjabka.csc.contents.item;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.item.Rarity;

import java.util.Set;

public class Midas extends BaseActiveItem {
    public Midas(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON)
                .useCooldown(100)
                .component(CSCComponents.ABILITY, new AbilityComponent(CSCAbilities.MIDAS.getId(), 0, Set.of(ActivationType.INTERACTION)))
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(1250, ShopCategory.MAGIC))
        );
    }
}