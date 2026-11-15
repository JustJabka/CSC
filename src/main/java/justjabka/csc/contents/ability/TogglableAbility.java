package justjabka.csc.contents.ability;

import justjabka.csc.types.AbilityCategory;

public class TogglableAbility extends Ability {

    public TogglableAbility(AbilityCategory category) {
        super(category);
    }

    @Override
    public int tickDuration(int duration) {
        return duration + 1;
    }
}
