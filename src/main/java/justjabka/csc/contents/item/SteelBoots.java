package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.item.SteelBootsAbility;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SteelBoots extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 4;
    private static final double BASE_DAMAGE = 1;

    private static final double GRAVITY_MODIFIER = Integer.MAX_VALUE;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = Integer.MAX_VALUE;
    private static final double JUMP_STRENGTH_MODIFIER = Integer.MIN_VALUE;
    private static final double MOVEMENT_SPEED_MODIFIER = 0.25;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            Attributes.GRAVITY, new AttributeModifier(
                    getKey(),
                    GRAVITY_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.JUMP_STRENGTH, new AttributeModifier(
                    getKey(),
                    JUMP_STRENGTH_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                    getKey(),
                    KNOCKBACK_RESISTANCE_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    getKey(),
                    MOVEMENT_SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
    );

    public SteelBoots(Properties properties) {
        super(properties.component(CSCComponents.SHOP_ITEM, new ShopItemComponent(3500, ShopCategory.TACTIC)));
    }

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "steel_boots");
    }

    @Override
    public int getCooldown() {
        return TimeHandler.secondsToTicks(45);
    }

    @Override
    public int getDuration() {
        return TimeHandler.secondsToTicks(10);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new SteelBootsAbility(getKey(), getDuration(), ACTIVE_MODIFIERS);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.2",
                Component.translatable("attribute.name.movement_speed"),
                DescriptionHandler.wrapDecimalAsPercent(MOVEMENT_SPEED_MODIFIER)
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.steel_boots.description.3").withStyle(ChatFormatting.GRAY));
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
