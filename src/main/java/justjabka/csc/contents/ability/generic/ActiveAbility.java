package justjabka.csc.contents.ability.generic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public abstract class ActiveAbility {

    protected final Player player;
    protected final InteractionHand hand;
    protected int remainingTicks;
    protected boolean togglable;

    public ActiveAbility(Player player, InteractionHand hand, int durationTicks) {
        this.player = player;
        this.hand = hand;
        this.remainingTicks = durationTicks;
    }

    public void tick() {
        onTick();

        if (!togglable) {
            remainingTicks--;
        }
    }

    public void refresh(ActiveAbility other) {
        this.remainingTicks = other.remainingTicks;
    }

    public boolean isTogglable() {
        return togglable;
    }

    public boolean isEnded() {
        return !togglable && remainingTicks <= 0;
    }

    public boolean shouldEnd() {
        return false;
    }

    public abstract void onStart();
    protected abstract void onTick();
    public abstract void onEnd();
}