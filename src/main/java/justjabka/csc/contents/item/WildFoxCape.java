package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
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

public class WildFoxCape extends BaseActiveTrinketItem {
    private static final double BASE_DODGE_CHANCE = 0.25;
    private static final double BASE_MOVEMENT_SPEED = 0.05;
    private static final double BASE_ATTACK_SPEED = 0.05;

    public WildFoxCape(Properties properties) {
        super(properties.component(CSCComponents.SHOP_ITEM, new ShopItemComponent(3800, ShopCategory.SURVIVABILITY)));
    }

    @Override
    public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity, Identifier key, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        AttributeHandler.addTrinketModifier(
                BASE_DODGE_CHANCE,
                CSCAttributes.DODGE_CHANCE,
                AttributeModifier.Operation.ADD_VALUE, key, consumer
        );
        AttributeHandler.addTrinketModifier(
                BASE_ATTACK_SPEED,
                Attributes.ATTACK_SPEED,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, key, consumer
        );
        AttributeHandler.addTrinketModifier(
                BASE_MOVEMENT_SPEED,
                Attributes.MOVEMENT_SPEED,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, key, consumer
        );
    }
}
