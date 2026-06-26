package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class ThornsAbility extends BaseActiveAbility {
    private final AttributeModifier DAMAGE_REFLECTION_MODIFIER = new AttributeModifier(
            getId(),
            0.5,
            AttributeModifier.Operation.ADD_VALUE
    );

    public ThornsAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        String reflectionPercent = DescriptionHandler.wrapDecimalAsPercent(DAMAGE_REFLECTION_MODIFIER.amount());
        textConsumer.accept(Component.translatable("item.csc.thorns.description", reflectionPercent, DescriptionHandler.MAGICAL_DAMAGE).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifier(
                player,
                CSCAttributes.DAMAGE_REFLECTION_PERCENT,
                DAMAGE_REFLECTION_MODIFIER
        );

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        if (!(ctx.player instanceof ServerPlayer serverPlayer)) return;

        serverPlayer.level().sendParticles (
                ParticleTypes.WITCH,
                serverPlayer.getX(),
                serverPlayer.getBoundingBox().minY + 1,
                serverPlayer.getZ(),
                5,
                0.1, 0.5, 0.1,
                1
        );
    }

    @Override
    public void onEnd() {
        AttributeHandler.removeModifier(
                ctx.player,
                CSCAttributes.DAMAGE_REFLECTION_PERCENT,
                DAMAGE_REFLECTION_MODIFIER
        );
    }
}