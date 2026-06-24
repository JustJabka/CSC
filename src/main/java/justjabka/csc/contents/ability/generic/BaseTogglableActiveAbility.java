package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.types.AbilityContext;
import net.minecraft.resources.Identifier;

public abstract class BaseTogglableActiveAbility extends BaseActiveAbility {

    public BaseTogglableActiveAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    protected void updateDuration() {
        PlayerData data = getPlayerData();

        duration++;
        ctx.player.setAttached(CSCAttachments.PLAYER_DATA, data.updateAbility(getId(), duration));
    }

    @Override
    public boolean isEnded() {
        return false;
    }
}