package justjabka.csc.contents.item.consumable;

import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseConsumable;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Consumer;

public class AstralDust extends BaseConsumable {
    private static final double RADIUS = 15;
    private static final int COOLDOWN = 32;

    public AstralDust(Properties properties) {
        super(properties.useCooldown(COOLDOWN)
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(100, ShopCategory.TACTIC))
        );
    }

    @Override
    protected void onUse(Level level, Player player, InteractionHand hand, ItemStack stack) {
        List<Player> nearbyPlayers = getSuitablePlayers(level, player);

        for (Player target : nearbyPlayers) {
            if (!(target instanceof ServerPlayer serverTarget)) continue;

            serverTarget.setInvisible(false);
            serverTarget.removeEffect(MobEffects.INVISIBILITY);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1, 1);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.astral_dust.description", RADIUS).withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected boolean canActivate(Player player, ItemStack stack, Level level) {
        return !getSuitablePlayers(level, player).isEmpty();
    }

    private static @NonNull List<Player> getSuitablePlayers(Level level, Player player) {
        AABB searchBox = player.getBoundingBox().inflate(RADIUS);

        return level.getEntitiesOfClass(Player.class, searchBox, target -> {
            if (target == player) return false;
            if (!target.isAlive()) return false;

            return target.isInvisible();
        });
    }
}
