package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.MagicProtectionClockAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MagicProtectionClock extends BaseActiveTrinketItem {
    private static final double BASE_MAGIC_RESISTANCE = 0.25;

    public MagicProtectionClock(Properties properties) {
        super(
            properties.rarity(Rarity.RARE),
            new ActiveItemConfig(
                true,
                true,
                60,
                10
            )
        );
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new MagicProtectionClockAbility(getSecondsToTicks(config.duration));
    }

    @Override
    public void forEachTrinketModifier(
            ItemStack stack,
            TrinketSlotAccess slot,
            LivingEntity entity,
            Identifier key,
            BiConsumer<Holder<Attribute>, AttributeModifier> consumer
    ) {
        AttributeModifier magicResistanceModifier = new AttributeModifier(
                key.withSuffix("%s/magic_resistance".formatted(CSC.MOD_ID)),
                BASE_MAGIC_RESISTANCE,
                AttributeModifier.Operation.ADD_VALUE
        );

        consumer.accept(CSCAttributes.MAGIC_RESISTANCE, magicResistanceModifier);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("other.csc.duration", config.duration).withStyle(ChatFormatting.GREEN));
        textConsumer.accept(Component.translatable("item.csc.magic_protection_clock.description", MAGICAL_DAMAGE, config.duration).withStyle(ChatFormatting.GRAY));
    }
}
