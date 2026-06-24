package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.Set;
import java.util.function.BiConsumer;

public class DarkCape extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 4;
    private static final double BASE_DAMAGE = 4;

    public DarkCape(Properties properties) {
        super(properties.rarity(Rarity.EPIC).useCooldown(45)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.DARK_CAPE.getId(), TimeHandler.secondsToTicks(12), Set.of(ActivationType.TRINKET))
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(3800, ShopCategory.DAMAGE))
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
