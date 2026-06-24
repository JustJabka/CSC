package justjabka.csc.contents.item;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LifeShield extends Item {
    private static final double BASE_HEALTH = 10;
    private static final double BASE_DAMAGE_PENALTY = -0.02;

    public LifeShield(Properties properties) {
        super(properties
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
                .equippableUnswappable(EquipmentSlot.OFFHAND)
                .useCooldown(32)
                .component(
                        CSCComponents.ABILITY,
                        new AbilityComponent(CSCAbilities.LIFE_SHIELD.getId(), TimeHandler.secondsToTicks(20), Set.of(ActivationType.ON_BLOCK))
                )
                .delayedComponent(DataComponents.BLOCKS_ATTACKS,
                        context -> new BlocksAttacks(
                                0.25f,
                                1f,
                                List.of(new BlocksAttacks.DamageReduction(
                                                90f,
                                                Optional.empty(),
                                                0f,
                                                1f
                                )),
                                new BlocksAttacks.ItemDamageFunction(
                                        3f,
                                        1f,
                                        1f
                                ),
                                Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                Optional.of(CSCSounds.ITEM_LIFE_SHIELD_BLOCK),
                                Optional.of(CSCSounds.ITEM_LIFE_SHIELD_BLOCK)
                        )
                )
                .attributes(ItemAttributeModifiers.builder()
                        .add(
                                Attributes.MAX_HEALTH,
                                new AttributeModifier(
                                        AttributeHandler.BASE_MAX_HEALTH_ID,
                                        BASE_HEALTH,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.HAND
                        )
                        .add(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        AttributeHandler.BASE_ATTACK_DAMAGE_ID,
                                        BASE_DAMAGE_PENALTY,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                ),
                                EquipmentSlotGroup.HAND
                        )
                        .add(
                                CSCAttributes.MAGIC_DAMAGE,
                                new AttributeModifier(
                                        AttributeHandler.BASE_MAGIC_DAMAGE_ID,
                                        BASE_DAMAGE_PENALTY,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                ),
                                EquipmentSlotGroup.HAND
                        )
                        .build()
                )
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2500, ShopCategory.SURVIVABILITY))
        );
    }
}
