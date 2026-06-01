package justjabka.csc.contents.item.generic;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AbilityContext;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class BaseActiveItem extends BaseItem {
    // Ability Settings
    protected Identifier getKey() {
        return null;
    }
    protected abstract int getCooldown();
    protected abstract int getDuration();
    protected abstract BaseActiveAbility getAbility();

    protected boolean haveCooldown() {
        return getCooldown() > 0;
    }

    protected boolean haveDuration() {
        return getDuration() > 0;
    }

    protected boolean haveAbility() {
        return getAbility() != null;
    }

    public BaseActiveItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(
            @NonNull ItemStack stack,
            @NonNull TooltipContext context,
            @NonNull TooltipDisplay displayComponent,
            Consumer<Component> textConsumer,
            @NonNull TooltipFlag type
    ) {
        if (haveCooldown()) {
            textConsumer.accept(Component.translatable("other.csc.cooldown", getCooldown()).withStyle(ChatFormatting.YELLOW));
        }

        if (haveDuration()) {
            textConsumer.accept(Component.translatable("other.csc.duration", getDuration()).withStyle(ChatFormatting.GREEN));
        }
    }

    // Interactions
    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot());
        return tryActivate(ctx);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack item,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot(), item, target);
        return tryActivate(ctx);
    }

    // Activation
    public InteractionResult tryActivate(AbilityContext ctx) {
        Player player = ctx.player;
        ItemStack item = ctx.getItem();

        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(item, player)) return InteractionResult.FAIL;
        if (!canActivate(ctx)) return InteractionResult.FAIL;

        applyCooldown(item, player);
        onUse(ctx);

        return InteractionResult.SUCCESS;
    }

    private void applyCooldown(ItemStack item, Player player) {
        if (!haveCooldown()) return;

        player.getCooldowns().addCooldown(item, getSecondsToTicks(getCooldown()));
    }

    private boolean isOnCooldown(ItemStack item, Player player) {
        boolean isItemOnCooldown = player.getCooldowns().isOnCooldown(item);
        boolean haveCooldown = haveCooldown() && isItemOnCooldown;

        if (!haveCooldown) return false;

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
        return true;
    }

    protected boolean canActivate(AbilityContext ctx) {
        return true;
    }

    protected void onUse(AbilityContext ctx) {
        if (haveAbility()) activateAbility(ctx);
    }

    private void activateAbility(AbilityContext ctx) {
        AbilityHandler handler = ctx.player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        BaseActiveAbility ability = getAbility();

        handler.addAbility(ability, ctx);
    }
}