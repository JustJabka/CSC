package justjabka.csc.contents.ability.generic;

import justjabka.csc.handlers.AbilityContext;

public abstract class ActiveAbility {
    protected AbilityContext ctx;
    protected final boolean togglable;
    protected int remainingTicks;

    protected ActiveAbility(boolean togglable, int duration) {
        this.togglable = togglable;
        this.remainingTicks = duration;
    }

    public void start(AbilityContext ctx) {
        this.ctx = ctx;
        onStart();
    }

    public void tick() {
        onTick();

        if (!togglable) {
            remainingTicks--;
        }
    }

    public final void end() {
        onEnd();
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
    public abstract void onTick();
    public abstract void onEnd();
}