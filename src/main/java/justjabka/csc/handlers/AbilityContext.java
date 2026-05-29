package justjabka.csc.handlers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AbilityContext {
    public final Player player;
    public final InteractionHand hand;
    public final Level level;

    private final @Nullable LivingEntity target;

    public AbilityContext(Player player, InteractionHand hand) {
        this(player, hand, null);
    }

    public AbilityContext(Player player, InteractionHand hand, @Nullable LivingEntity target) {
        this.player = player;
        this.hand = hand;
        this.level = player.level();
        this.target = target;
    }

    public ItemStack getStack() {
        return player.getItemInHand(hand);
    }

    public Optional<LivingEntity> getTarget() {
        return Optional.ofNullable(target);
    }
}