package justjabka.csc.contents.item;

import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class PhoenixFeather extends BaseActiveItem {
    public PhoenixFeather(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("item.csc.phoenix_feather.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        int cooldown = 35;
        double horizontalStrength = 1.6d;
        double verticalStrength = 0.6d;

        ItemStack stack = player.getItemInHand(hand);

        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(player, stack)) return InteractionResult.FAIL;

        // Set Cooldown
        player.getCooldowns().addCooldown(stack, getTicksToSeconds(cooldown));

        // Server-side logic
        if (player instanceof ServerPlayer serverPlayer) {
            // Apply Impulse
            Vec3 impulse = getImpulse(serverPlayer, horizontalStrength, verticalStrength);
            player.setDeltaMovement(impulse);
            player.hurtMarked = true;

            // Add Particles
            serverPlayer.level().sendParticles (
                    ParticleTypes.GUST_EMITTER_SMALL,
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    1,
                    0.3, 0.3, 0.3,
                    0.02
            );
        }

        // Play Sound
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_PHOENIX_FEATHER, SoundSource.PLAYERS, 1f, 1f);

        return InteractionResult.SUCCESS;
    }

    private static Vec3 getImpulse(ServerPlayer player, double horizontalStrength, double verticalStrength) {
        Vec3 lookAngle = player.getLookAngle().normalize();

        Vec3 horizontal = new Vec3(
                lookAngle.x * horizontalStrength,
                0,
                lookAngle.z * horizontalStrength
        );

        Vec3 vertical = new Vec3(
                0,
                verticalStrength,
                0
        );

        return horizontal.add(vertical);
    }
}