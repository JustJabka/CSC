package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.handlers.AbilityContext;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.resources.Identifier;

public abstract class ActiveAbility {
    protected AbilityContext ctx;
    protected final Identifier key;
    protected final boolean togglable;
    protected int duration;

    protected ActiveAbility(Identifier key, boolean togglable, int duration) {
        this.key = key;
        this.togglable = togglable;
        this.duration = duration;
    }

    public void start(AbilityContext ctx) {
        this.ctx = ctx;
        onStart();
    }

    @SuppressWarnings("UnstableApiUsage")
    public void tick() {
        PlayerData data = ctx.player.getAttached(CSCAttachments.PLAYER_DATA);
        if (data == null) data = PlayerData.DEFAULT;

        onTick();

        if (togglable) {
            duration++;
        } else {
            duration--;
        }

        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.updateAbility(key, duration));
    }

    public final void end() {
        onEnd();

        PlayerData data = ctx.player.getAttached(CSCAttachments.PLAYER_DATA);
        if (data == null) return;

        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.removeAbility(key));
    }

    public int getRemainingSeconds() {
        return duration / 20;
    }

    public void refresh(ActiveAbility other) {
        this.duration = other.duration;
    }

    public boolean isTogglable() {
        return togglable;
    }

    public boolean isEnded() {
        return !togglable && duration <= 0;
    }

    public boolean shouldEnd() {
        return false;
    }

    public abstract void onStart();
    public abstract void onTick();
    public abstract void onEnd();
}