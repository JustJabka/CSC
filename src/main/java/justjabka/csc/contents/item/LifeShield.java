package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.item.LifeShieldAbility;
import justjabka.csc.contents.item.generic.ActivatableItem;
import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static justjabka.csc.handlers.DescriptionHandler.wrapDecimalAsPercent;

public class LifeShield extends Item implements ActivatableItem, ShopItem {
    private static final double DAMAGE_MULTIPLIER_MODIFIER = -0.05;
    private static final float HEAL_AMOUNT = 10;
    private static final float ABSORPTION_AMOUNT = 8;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                    getKey(),
                    DAMAGE_MULTIPLIER_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MAX_ABSORPTION, new AttributeModifier(
                    getKey(),
                    ABSORPTION_AMOUNT,
                    AttributeModifier.Operation.ADD_VALUE
            )
    );

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "life_shield");
    }

    @Override
    public int getCooldown() {
        return TimeHandler.secondsToTicks(32);
    }

    @Override
    public int getDuration() {
        return TimeHandler.secondsToTicks(20);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new LifeShieldAbility(getKey(), getDuration(), ACTIVE_MODIFIERS, HEAL_AMOUNT, ABSORPTION_AMOUNT);
    }

    @Override
    public int getPrice() {
        return 2500;
    }

    @Override
    public ShopCategory getCategory() {
        return ShopCategory.SURVIVABILITY;
    }

    public LifeShield(Properties properties) {
        super(properties
                .rarity(Rarity.RARE)
                .stacksTo(1)
                .equippableUnswappable(EquipmentSlot.OFFHAND)
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
        );
    }

    @Override
    public void appendHoverText(
            @NonNull ItemStack stack,
            @NonNull TooltipContext context,
            @NonNull TooltipDisplay displayComponent,
            Consumer<Component> textConsumer,
            @NonNull TooltipFlag type
    ) {
        getDescription(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.1", HEAL_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.2", ABSORPTION_AMOUNT).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.life_shield.description.3", wrapDecimalAsPercent(DAMAGE_MULTIPLIER_MODIFIER)).withStyle(ChatFormatting.GRAY));
    }
}
