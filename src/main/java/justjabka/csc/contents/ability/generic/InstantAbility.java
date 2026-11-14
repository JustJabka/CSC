package justjabka.csc.contents.ability.generic;

import justjabka.csc.types.AbilityContext;
import net.minecraft.resources.Identifier;

public abstract class InstantAbility extends ActiveAbility {
    public InstantAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void start() {
        onStart();
        forceEnd();
    }

    @Override
    public void onEnd() {}

    @Override
    public void onTick() {}
}
