package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.ThornsAbility;
import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.ActiveItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
    // Item Properties
    private static final double DAMAGE_REFLECTION = 0.6;

    public Thorns(Properties properties) {
        super(properties
                .rarity(Rarity.RARE),
                new ActiveItemConfig(
                        true,
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
    protected ActiveAbility createAbility() {
        return new ThornsAbility(getSecondsToTicks(config.duration), DAMAGE_REFLECTION);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }
}