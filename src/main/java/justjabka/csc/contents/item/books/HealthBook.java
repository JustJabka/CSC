package justjabka.csc.contents.item.books;

import justjabka.csc.contents.item.generic.BaseConsumable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class HealthBook extends BaseConsumable {
    private static final double HEALTH_ON_USE = 4.0;

    public HealthBook(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("item.csc.health_book.description", HEALTH_ON_USE).withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected void onUse(Level level, Player player, InteractionHand hand, ItemStack stack) {
        AttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        double currentMaxHealth = attribute.getValue();

        attribute.setBaseValue(currentMaxHealth + HEALTH_ON_USE);

        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 1f);
    }
}