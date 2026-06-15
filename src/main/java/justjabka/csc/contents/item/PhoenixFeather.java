package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class PhoenixFeather extends BaseActiveTrinketItem {
    private static final double HORIZONTAL_STRENGTH = 1.6;
    private static final double VERTICAL_STRENGTH = 0.6;

    @Override
    public Identifier getKey() {
        return null;
    }

    @Override
    public int getCooldown() {
        return 35;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return null;
    }

    public PhoenixFeather(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.phoenix_feather.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onUse(AbilityContext ctx) {
        Player player = ctx.player;

        // Server-side logic
        if (player instanceof ServerPlayer serverPlayer) {
            // Apply Impulse
            Vec3 impulse = getImpulse(serverPlayer);
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

    private static Vec3 getImpulse(ServerPlayer player) {
        Vec3 lookAngle = player.getLookAngle().normalize();

        Vec3 horizontal = new Vec3(
                lookAngle.x * PhoenixFeather.HORIZONTAL_STRENGTH,
                0,
                lookAngle.z * PhoenixFeather.HORIZONTAL_STRENGTH
        );

        Vec3 vertical = new Vec3(
                0,
                PhoenixFeather.VERTICAL_STRENGTH,
                0
        );

        return horizontal.add(vertical);
    }
}