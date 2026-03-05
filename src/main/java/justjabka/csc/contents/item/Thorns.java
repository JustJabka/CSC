package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.ThornsAbility;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Thorns extends BaseActiveItem {
    // Item Properties
    private static final int COOLDOWN = 45;
    private static final int DURATION = 5;
    private static final double DAMAGE_REFLECTION = 0.6;

    public Thorns(Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", COOLDOWN).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("other.csc.duration", DURATION).withStyle(ChatFormatting.GREEN));
        textConsumer.accept(Component.translatable("item.csc.thorns.description", wrapDecimalAsPercent(DAMAGE_REFLECTION)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(player, stack)) {
            player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
            return InteractionResult.FAIL;
        }

        // Set Cooldown
        player.getCooldowns().addCooldown(stack, getSecondsToTicks(COOLDOWN));

        // Activate
        AbilityHandler handler = player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        handler.addAbility(new ThornsAbility(player, hand, getSecondsToTicks(DURATION), DAMAGE_REFLECTION));

        // Play Sound
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);

        return InteractionResult.SUCCESS;
    }
}