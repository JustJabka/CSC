package justjabka.csc.contents.item.generic;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface ActivatableItem {
    Identifier getKey();
    int getCooldown();
    int getDuration();
    BaseActiveAbility getAbility();

    default boolean haveCooldown() {
        return getCooldown() > 0;
    }

    default boolean haveDuration() {
        return getDuration() > 0;
    }

    default boolean haveAbility() {
        return getAbility() != null;
    }

    // Description
    default void getAbilityDescription(
            @NonNull ItemStack stack,
            Item.@NonNull TooltipContext context,
            @NonNull TooltipDisplay displayComponent,
            Consumer<Component> textConsumer,
            @NonNull TooltipFlag type
    ) {
        if (haveCooldown()) {
            textConsumer.accept(Component.translatable("other.csc.cooldown", TimeHandler.autoConvertTicks(getCooldown())).withStyle(ChatFormatting.YELLOW));
        }

        if (haveDuration()) {
            textConsumer.accept(Component.translatable("other.csc.duration", TimeHandler.autoConvertTicks(getDuration())).withStyle(ChatFormatting.GREEN));
        }
    }

    // Activation
    default InteractionResult tryActivate(AbilityContext ctx) {
        Player player = ctx.player;
        ItemStack item = ctx.getItem();

        if (player.level().isClientSide()) return InteractionResult.PASS;
        if (isOnCooldown(item, player)) return InteractionResult.FAIL;
        if (!canActivate(ctx)) return InteractionResult.FAIL;

        applyCooldown(item, player);
        onUse(ctx);

        if (player.isUsingItem()) {
            player.stopUsingItem();
        }

        return InteractionResult.SUCCESS;
    }

    default boolean canActivate(AbilityContext ctx) {
        return true;
    }


    // Cooldown
    default void applyCooldown(ItemStack item, Player player) {
        if (!haveCooldown()) return;

        player.getCooldowns().addCooldown(item, getCooldown());
    }

    default boolean isOnCooldown(ItemStack item, Player player) {
        boolean isItemOnCooldown = player.getCooldowns().isOnCooldown(item);
        boolean haveCooldown = haveCooldown() && isItemOnCooldown;

        if (!haveCooldown) return false;

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
        return true;
    }


    // Use
    default void onUse(AbilityContext ctx) {
        if (haveAbility()) activateAbility(ctx);
    }

    default void activateAbility(AbilityContext ctx) {
        AbilityHandler handler = ctx.player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        BaseActiveAbility ability = getAbility();

        handler.addAbility(ability, ctx);
    }
}
