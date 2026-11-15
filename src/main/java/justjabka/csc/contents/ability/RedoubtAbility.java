package justjabka.csc.contents.ability;

import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class RedoubtAbility extends Ability implements ShardAffected {

    private static final double INCOMING_DAMAGE_MULTIPLIER_MODIFIER = -0.35;
    private static final double KNOCKBACK_RESISTANCE_MODIFIER = 1;
    private static final float ABSORPTION_AMOUNT_SHARD_BONUS = 0.1f;

    public RedoubtAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, INCOMING_DAMAGE_MULTIPLIER_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.redoubt.description.1", DescriptionHandler.wrapDecimalAsPercent(INCOMING_DAMAGE_MULTIPLIER_MODIFIER)).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.redoubt.description.2", DescriptionHandler.wrapDecimalAsPercent(KNOCKBACK_RESISTANCE_MODIFIER)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        if (hasShard(player)) giveShardBonus(player);

        ctx.level.playSound(null, player.blockPosition(), CSCSounds.ABILITY_REDOUBT, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public boolean onTick(AbilityContext ctx) {
        Player player = ctx.player;

        if (!(ctx.level instanceof ServerLevel serverLevel)) return true;

        serverLevel.sendParticles(
                ParticleTypes.POOF,
                player.getX(),
                player.getBoundingBox().minY + 1,
                player.getZ(),
                1,
                0.25, 0.5, 0.25,
                0
        );

        return true;
    }

    private void giveShardBonus(Player player) {
        float currentAmount = player.getAbsorptionAmount();
        float bonusAmount = player.getMaxHealth() * ABSORPTION_AMOUNT_SHARD_BONUS;

        addAttributeModifier(Attributes.MAX_ABSORPTION, bonusAmount, AttributeModifier.Operation.ADD_VALUE);

        player.setAbsorptionAmount(currentAmount + bonusAmount);
    }
}
