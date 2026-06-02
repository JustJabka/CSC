package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCAttributes;
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

    public WildFoxCape(Properties properties) {
        super(properties);
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected int getDuration() {
        return 0;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return null;
    }

    @Override
    public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity, Identifier key, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        AttributeModifier dodgeChanceModifier = new AttributeModifier(
                key.withSuffix("%s/dodge_chance".formatted(CSC.MOD_ID)),
                BASE_DODGE_CHANCE,
                AttributeModifier.Operation.ADD_VALUE
        );
        AttributeModifier movementSpeedModifier = new AttributeModifier(
                key.withSuffix("%s/movement_speed".formatted(CSC.MOD_ID)),
                BASE_MOVEMENT_SPEED,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        consumer.accept(CSCAttributes.DODGE_CHANCE, dodgeChanceModifier);
        consumer.accept(Attributes.MOVEMENT_SPEED, movementSpeedModifier);
    }
}
