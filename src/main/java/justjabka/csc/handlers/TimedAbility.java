package justjabka.csc.handlers;

import net.minecraft.world.entity.player.Player;

public abstract class TimedAbility {

    protected int remainingTicks;
    protected final Player player;

    public TimedAbility(Player player, int durationTicks) {
        this.player = player;
        this.remainingTicks = durationTicks;
    }

    public void tick() {
        if (remainingTicks > 0) {
            remainingTicks--;
            onTick();
        }

        if (remainingTicks == 0) {
            onEnd();
        }
    }

    public boolean isFinished() {
        return remainingTicks <= 0;
    }

    protected abstract void onStart();
    protected abstract void onTick();
    protected abstract void onEnd();
}