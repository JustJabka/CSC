package justjabka.csc.contents.ability.generic;

import justjabka.csc.types.AbilityContext;
import net.minecraft.resources.Identifier;

public abstract class BaseTogglableActiveAbility extends BaseActiveAbility {

    public BaseTogglableActiveAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    protected int updateDuration() {
        return ++duration;
    }

    @Override
    public boolean isEnded() {
        return false;
    }
}