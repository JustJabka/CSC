package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.ThornsAbility;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCAttachments;
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
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Thorns extends BaseActiveItem {
    // Item Properties
    private static final double DAMAGE_REFLECTION = 0.6;

    public Thorns(Properties properties) {
        super(properties
                .rarity(Rarity.RARE),
                new ActiveItemConfig(
                        true,
                        45,
                        5
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("other.csc.duration", config.duration).withStyle(ChatFormatting.GREEN));
        textConsumer.accept(Component.translatable("item.csc.thorns.description", wrapDecimalAsPercent(DAMAGE_REFLECTION)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    protected void onActivation(Level level, Player player, InteractionHand hand, ItemStack stack) {
        // Activate Ability
        AbilityHandler handler = player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        handler.addAbility(new ThornsAbility(player, hand, getSecondsToTicks(config.duration), DAMAGE_REFLECTION));

        // Play Sound
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_THORNS, SoundSource.PLAYERS, 1f, 1f);
    }
}