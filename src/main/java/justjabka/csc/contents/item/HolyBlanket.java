package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
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
import net.minecraft.world.item.Rarity;

import java.util.function.BiConsumer;

public class HolyBlanket extends BaseActiveTrinketItem {
    private static final double BASE_DAMAGE_PENALTY = -0.02;

    public HolyBlanket(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON)
                .useCooldown(42)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.HOLY_BLANKET.getId(), TimeHandler.secondsToTicks(1))
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(3800, ShopCategory.TACTIC))
        );
    }

    @Override
    public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity, Identifier key, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        AttributeHandler.addTrinketModifier(
                BASE_DAMAGE_PENALTY,
                Attributes.ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                key,
                consumer
        );
        AttributeHandler.addTrinketModifier(
                BASE_DAMAGE_PENALTY,
                CSCAttributes.MAGIC_DAMAGE,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                key,
                consumer
        );
    }
}
