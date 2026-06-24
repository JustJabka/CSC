package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.types.AbilityContext;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class BaseActiveAbility {
    protected final Identifier id;
    protected int duration;
    protected final AbilityContext ctx;
    protected boolean ended = false;

    public BaseActiveAbility(Identifier id, int duration, AbilityContext ctx) {
        this.id = id;
        this.duration = duration;
        this.ctx = ctx;
    }

    public Identifier getId() {
        return this.id;
    }

    public void start() {
        onStart();
    }

    public void tick() {
        onTick();
        updateDuration();
    }

    public final void end() {
        onEnd();

        PlayerData data = getPlayerData();
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.removeAbility(getId()));
    }

    public void refresh(BaseActiveAbility ability) {
        this.duration = ability.duration;
    }

    public boolean isEnded() {
        return this.ended || duration <= 0;
    }

    public void forceEnd() {
        this.ended = true;
    }

    public boolean shouldEnd() {
        return false;
    }

    public boolean canActivate(AbilityContext ctx) {
        return true;
    }

    public boolean isPlayerValid() {
        return !ctx.player.isDeadOrDying();
    }

    public abstract void onStart();
    public abstract void onTick();
    public abstract void onEnd();

    protected void updateDuration() {
        PlayerData data = getPlayerData();

        duration--;
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.updateAbility(getId(), duration));
    }

    protected @NonNull PlayerData getPlayerData() {
        PlayerData data = ctx.player.getAttached(CSCAttachments.PLAYER_DATA);
        if (data == null) data = PlayerData.DEFAULT;

        return data;
    }
}