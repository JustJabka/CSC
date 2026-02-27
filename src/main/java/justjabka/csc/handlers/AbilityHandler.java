package justjabka.csc.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbilityHandler {

    private final List<TimedAbility> activeAbilities = new ArrayList<>();

    public void addAbility(TimedAbility ability) {
        ability.onStart();
        activeAbilities.add(ability);
    }

    public void tick() {
        Iterator<TimedAbility> iterator = activeAbilities.iterator();

        while (iterator.hasNext()) {
            TimedAbility ability = iterator.next();
            ability.tick();

            if (ability.isFinished()) {
                iterator.remove();
            }
        }
    }
}