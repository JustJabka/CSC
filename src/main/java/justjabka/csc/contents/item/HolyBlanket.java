package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.item.HolyBlanketAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.contents.item.generic.ShopItem;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
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

public class HolyBlanket extends BaseActiveTrinketItem implements ShopItem {
    private static final int PROTECTION_DURATION = TimeHandler.secondsToTicks(20);
    private static final float WEAK_PROTECTION_HEALTH_MULTIPLIER = 0.3f;
    private static final float STRONG_PROTECTION_HEALTH_MULTIPLIER = 0.6f;
    private static final double BASE_DAMAGE_PENALTY = -0.02;

    public HolyBlanket(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "holy_blanket");
    }

    @Override
    public int getCooldown() {
        return TimeHandler.secondsToTicks(42);
    }

    @Override
    public int getDuration() {
        return TimeHandler.secondsToTicks(1);
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new HolyBlanketAbility(
                getKey(),
                getDuration(),
                PROTECTION_DURATION,
                STRONG_PROTECTION_HEALTH_MULTIPLIER
        );
    }

    @Override
    public int getPrice() {
        return 3800;
    }

    @Override
    public ShopCategory getCategory() {
        return ShopCategory.TACTIC;
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

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.2",
                DescriptionHandler.wrapDecimalAsPercent(WEAK_PROTECTION_HEALTH_MULTIPLIER),
                DescriptionHandler.MAX_HEALTH
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.3").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.4",
                DescriptionHandler.wrapDecimalAsPercent(STRONG_PROTECTION_HEALTH_MULTIPLIER),
                DescriptionHandler.MAX_HEALTH
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.5").withStyle(ChatFormatting.GRAY));
    }

    public void applyWeakProtection(Player player) {
        player.setHealth(player.getMaxHealth() * WEAK_PROTECTION_HEALTH_MULTIPLIER);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_HOLY_BLANKET_BREAK, SoundSource.PLAYERS, 1f, 1f);
    }
}
