package justjabka.csc.contents.item.generic;

import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseActiveItem extends Item {
    protected final ActiveItemConfig config;

    public BaseActiveItem(Properties properties, ActiveItemConfig config) {
        super(properties.stacksTo(1));
        this.config = config;
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        // Gates denesting method, my favorite yummy
        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(stack, player)) return InteractionResult.FAIL;
        if (!canActivate(level, player, hand, stack)) return InteractionResult.FAIL;

        // On Activation
        applyCooldown(stack, player);
        onActivation(level, player, hand, stack);

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // Gates denesting method, my favorite yummy
        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(stack, player)) return InteractionResult.FAIL;
        if (!canActivate(player, hand, target, stack)) return InteractionResult.FAIL;

        // On Activation
        applyCooldown(stack, player);
        onActivation(player, hand, target, stack);

        return InteractionResult.SUCCESS;
    }

    // Activation
    private void applyCooldown(ItemStack stack, Player player) {
        if (config.haveCooldown) {
            player.getCooldowns().addCooldown(stack, getSecondsToTicks(config.cooldown));
        }
    }

    private boolean isOnCooldown(ItemStack stack, Player player) {
        boolean isOnCooldown = player.getCooldowns().isOnCooldown(stack);

        if (config.haveCooldown && isOnCooldown) {
            // TODO: fix sound not playing
            player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
            return true;
        }

        return false;
    }


    protected boolean canActivate(
            Level level,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {
        return true;
    }

    protected boolean canActivate(
            Player player,
            InteractionHand hand,
            LivingEntity target,
            ItemStack stack
    ) {
        return true;
    }

    protected void onActivation(
            Level level,
            Player player,
            InteractionHand hand,
            ItemStack stack
    ) {}

    protected void onActivation(
            Player player,
            InteractionHand hand,
            LivingEntity target,
            ItemStack stack
    ) {}

    // Utils
    protected int getSecondsToTicks(int seconds) {
        return seconds * 20;
    }

    protected String wrapDecimalAsPercent(double value) {
        int percent = Math.toIntExact(Math.round(value * 100));
        return percent + "%";
    }

    protected boolean isClientSide(Player player) {
        return player.level().isClientSide();
    }
}