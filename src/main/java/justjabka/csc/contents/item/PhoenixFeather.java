package justjabka.csc.contents.item;

import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
    // Item Properties
    private static final double HORIZONTAL_STRENGTH = 1.6;
    private static final double VERTICAL_STRENGTH = 0.6;

    public PhoenixFeather(Properties properties) {
        super(properties
                .rarity(Rarity.UNCOMMON),
                new ActiveItemConfig(
                        true,
                        35,
                        0
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("item.csc.phoenix_feather.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    protected void onActivation(Level level, Player player, InteractionHand hand, ItemStack stack) {
        // Server-side logic
        if (player instanceof ServerPlayer serverPlayer) {
            // Apply Impulse
            Vec3 impulse = getImpulse(serverPlayer, HORIZONTAL_STRENGTH, VERTICAL_STRENGTH);
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