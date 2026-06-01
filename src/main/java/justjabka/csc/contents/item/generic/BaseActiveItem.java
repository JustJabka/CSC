package justjabka.csc.contents.item.generic;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AbilityContext;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseActiveItem extends BaseItem {
    protected final ActiveItemConfig config;
    public BaseActiveAbility getAbility() {
        return null;
    }

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

    // Activation
    private void applyCooldown(ItemStack item, Player player) {
        if (config.haveCooldown) {
            player.getCooldowns().addCooldown(item, getSecondsToTicks(config.cooldown));
        }
    }

    private boolean isOnCooldown(ItemStack item, Player player) {
        boolean isItemOnCooldown = player.getCooldowns().isOnCooldown(item);
        boolean haveCooldown = config.haveCooldown && isItemOnCooldown;

        if (haveCooldown) {
            player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
            return true;
        }

        return false;
    }

    protected boolean canActivate(AbilityContext ctx) {
        return true;
    }

    protected void onUse(AbilityContext ctx) {
        if (config.haveAbility) activateAbility(ctx);
    }

    private void activateAbility(AbilityContext ctx) {
        AbilityHandler handler = ctx.player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        BaseActiveAbility ability = getAbility();

        handler.addAbility(ability, ctx);
    }
}