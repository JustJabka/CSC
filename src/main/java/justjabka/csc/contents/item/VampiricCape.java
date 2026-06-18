package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class VampiricCape extends BaseActiveTrinketItem implements ShopItem {
    private static final double BASE_HEALTH = 10;
    private static final double BASE_DAMAGE = 2;
    private static final double BASE_PHYSICAL_LIFE_STEAL = 0.1;

    public VampiricCape(Properties properties) {
        super(properties);
    }

    @Override
    public Identifier getKey() {
        return null;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return null;
    }

    @Override
    public int getPrice() {
        return 3200;
    }

    @Override
    public ShopCategory getCategory() {
        return ShopCategory.SURVIVABILITY;
    }

    @Override
    public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity, Identifier key, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
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
        AttributeHandler.addTrinketModifier(
                BASE_PHYSICAL_LIFE_STEAL,
                CSCAttributes.PHYSICAL_LIFE_STEAL,
                AttributeModifier.Operation.ADD_VALUE, key, consumer
        );
    }
}
