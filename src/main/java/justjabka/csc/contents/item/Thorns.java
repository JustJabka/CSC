package justjabka.csc.contents.item;

import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Thorns extends BaseActiveItem {
    public Thorns(Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("item.csc.thorns.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        int cooldown = 45;
        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(player, stack)) return InteractionResult.FAIL;

        // Set Cooldown
        player.getCooldowns().addCooldown(stack, getTicksToSeconds(cooldown));

        // Activate

        // Play Sound
        player.level().playSound(null, target.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);

        return InteractionResult.SUCCESS;
    }
}