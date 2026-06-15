package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class BaseActiveAbility {
    protected final Identifier key;
    protected int duration;

    protected AbilityContext ctx;

    public BaseActiveAbility(Identifier key, int duration) {
        this.key = key;
        this.duration = duration;
    }

    public void start(AbilityContext ctx) {
        this.ctx = ctx;
        onStart();
    }

    public void tick() {
        onTick();

        PlayerData data = getPlayerData();
        updateDuration(data);
    }

    protected void updateDuration(PlayerData data) {
        duration--;
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.updateAbility(key, duration));
    }

    public final void end() {
        onEnd();

        PlayerData data = getPlayerData();
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.removeAbility(key));
    }

    public void refresh(BaseActiveAbility other) {
        this.duration = other.duration;
    }

    public boolean isEnded() {
        return duration <= 0;
    }

    public boolean shouldEnd() {
        return false;
    }

    public boolean isPlayerValid() {
        return !ctx.player.isDeadOrDying();
    }

    public abstract void onStart();
    public abstract void onTick();
    public abstract void onEnd();

    protected @NonNull PlayerData getPlayerData() {
        PlayerData data = ctx.player.getAttached(CSCAttachments.PLAYER_DATA);
        if (data == null) data = PlayerData.DEFAULT;

        return data;
    }
}