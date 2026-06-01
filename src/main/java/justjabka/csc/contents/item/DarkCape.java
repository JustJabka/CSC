package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.DarkCapeAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.ActiveItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DarkCape extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 4;
    private static final double BASE_DAMAGE = 1;

    private static final ActiveItemConfig activeItemConfig = new ActiveItemConfig(
            true,
            true,
            42,
            12
    );

    public DarkCape(Properties properties) {
        super(properties.rarity(Rarity.EPIC), activeItemConfig);
    }

    @Override
    public void forEachTrinketModifier(
            ItemStack stack,
            TrinketSlotAccess slot,
            LivingEntity entity,
            Identifier key,
            BiConsumer<Holder<Attribute>, AttributeModifier> consumer
    ) {
        AttributeModifier healthModifier = new AttributeModifier(
                key.withSuffix("%s/max_health".formatted(CSC.MOD_ID)),
                BASE_HEALTH,
                AttributeModifier.Operation.ADD_VALUE
        );
        AttributeModifier damageModifier = new AttributeModifier(
                key.withSuffix("%s/attack_damage".formatted(CSC.MOD_ID)),
                BASE_DAMAGE,
                AttributeModifier.Operation.ADD_VALUE
        );

        consumer.accept(Attributes.MAX_HEALTH, healthModifier);
        consumer.accept(Attributes.ATTACK_DAMAGE, damageModifier);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new DarkCapeAbility(getSecondsToTicks(config.duration));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("other.csc.duration", config.duration).withStyle(ChatFormatting.GREEN));
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.1",
                    wrapDecimalAsPercent(DarkCapeAbility.SPEED_MODIFIER.amount()),
                    wrapDecimalAsPercent(DarkCapeAbility.VULNERABILITY_MODIFIER.amount())
                ).withStyle(ChatFormatting.GRAY)
        );
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.2",
                    DarkCapeAbility.DAMAGE_MULTIPLIER,
                    PHYSICAL_DAMAGE
                ).withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }
}
