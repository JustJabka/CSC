package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseInstantAbility;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class PhoenixFeatherAbility extends BaseInstantAbility {
    private static final double HORIZONTAL_STRENGTH = 1.6;
    private static final double VERTICAL_STRENGTH = 0.6;

    public PhoenixFeatherAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.phoenix_feather.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart() {
        Player player = ctx.player;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

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

        // Play Sound
        serverPlayer.level().playSound(null, serverPlayer.blockPosition(), CSCSounds.ITEM_PHOENIX_FEATHER, SoundSource.PLAYERS, 1f, 1f);
    }

    private static Vec3 getImpulse(ServerPlayer player) {
        Vec3 lookAngle = player.getLookAngle().normalize();

        Vec3 horizontal = new Vec3(
                lookAngle.x * HORIZONTAL_STRENGTH,
                0,
                lookAngle.z * HORIZONTAL_STRENGTH
        );

        Vec3 vertical = new Vec3(
                0,
                VERTICAL_STRENGTH,
                0
        );

        return horizontal.add(vertical);
    }
}
