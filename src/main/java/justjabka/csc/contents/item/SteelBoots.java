package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class SteelBoots extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 4;
    private static final double BASE_DAMAGE = 1;

    public SteelBoots(Properties properties) {
        super(properties.useCooldown(45)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.STEEL_BOOTS.getId(), TimeHandler.secondsToTicks(10))
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(3500, ShopCategory.TACTIC))
        );
    }

    @Override
    public void forEachTrinketModifier(
            ItemStack stack,
            TrinketSlotAccess slot,
            LivingEntity entity,
            Identifier key,
            BiConsumer<Holder<Attribute>, AttributeModifier> consumer
    ) {
        AttributeHandler.addTrinketModifier(
                BASE_HEALTH,
                Attributes.MAX_HEALTH,
                AttributeModifier.Operation.ADD_VALUE, key, consumer
        );
        AttributeHandler.addTrinketModifier(
                BASE_DAMAGE,
                Attributes.ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_VALUE, key, consumer
        );
    }
}
