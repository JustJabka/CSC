package justjabka.csc.contents.ability;

import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;

public class InstantAbility extends Ability {

    public InstantAbility(AbilityCategory category) {
        super(category);
    }

    @Override
    public boolean shouldEnd(AbilityContext ctx) {
        return true;
    }
}
