package justjabka.csc.contents.item;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DarkGauntlet extends BaseActiveItem {
    private static final double BASE_DAMAGE = 2.0;

    public DarkGauntlet(Properties properties) {
        super(properties
                .rarity(Rarity.EPIC)
                .useCooldown(1)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.DARK_GAUNTLET.getId(), 0)
                )
                .attributes(ItemAttributeModifiers.builder()
                    .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                    BASE_ATTACK_DAMAGE_ID,
                                    BASE_DAMAGE,
                                    AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.HAND
                    )
                    .build()
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2800, ShopCategory.DAMAGE))
        );
    }
}