package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.item.MagicProtectionClockAbility;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttributes;
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
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static justjabka.csc.handlers.DescriptionHandler.MAGICAL_DAMAGE;
import static justjabka.csc.handlers.DescriptionHandler.wrapDecimalAsPercent;

public class MagicProtectionClock extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 10;
    private static final double BASE_MAGIC_RESISTANCE = 0.25;

    private final AttributeModifier MAGIC_RESISTANCE_MODIFIER = new AttributeModifier(
            getKey(),
            1,
            AttributeModifier.Operation.ADD_VALUE
    );

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "magic_protection_clock");
    }

    @Override
    public int getCooldown() {
        return TimeHandler.minutesToTicks(1);
    }

    @Override
    public int getDuration() {
        return TimeHandler.secondsToTicks(10);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new MagicProtectionClockAbility(
                getKey(),
                getDuration(),
                MAGIC_RESISTANCE_MODIFIER
        );
    }

    public MagicProtectionClock(Properties properties) {
        super(properties.rarity(Rarity.RARE)
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(4200, ShopCategory.ANTI_MAGE))
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
                BASE_MAGIC_RESISTANCE,
                CSCAttributes.MAGIC_RESISTANCE,
                AttributeModifier.Operation.ADD_VALUE, key, consumer
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.magic_protection_clock.description",
                MAGICAL_DAMAGE,
                wrapDecimalAsPercent(MAGIC_RESISTANCE_MODIFIER.amount())
        ).withStyle(ChatFormatting.GRAY));
    }
}
