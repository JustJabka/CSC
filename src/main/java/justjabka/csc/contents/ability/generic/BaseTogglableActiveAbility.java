package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.resources.Identifier;

public abstract class BaseTogglableActiveAbility extends BaseActiveAbility {
    protected BaseTogglableActiveAbility(Identifier key, int duration) {
        super(key, duration);
    }

    @Override
    protected void updateDuration(PlayerData data) {
        duration++;
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.updateAbility(key, duration));
    }

    @Override
    public boolean isEnded() {
        return false;
    }
}