package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class ThornsAbility extends Ability {
    private static final double DAMAGE_REFLECTION_AMOUNT = 0.5;

    public ThornsAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.DAMAGE_REFLECTION_PERCENT, DAMAGE_REFLECTION_AMOUNT, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        String reflectionPercent = DescriptionHandler.wrapDecimalAsPercent(DAMAGE_REFLECTION_AMOUNT);
        textConsumer.accept(Component.translatable("item.csc.thorns.description", reflectionPercent, DescriptionHandler.MAGICAL_DAMAGE).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public boolean onTick(AbilityContext ctx) {
        if (!(ctx.player instanceof ServerPlayer serverPlayer)) return true;

        serverPlayer.level().sendParticles (
                ParticleTypes.WITCH,
                serverPlayer.getX(),
                serverPlayer.getBoundingBox().minY + 1,
                serverPlayer.getZ(),
                5,
                0.1, 0.5, 0.1,
                1
        );

        return true;
    }
}