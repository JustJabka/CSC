package justjabka.csc.handlers;

import justjabka.csc.contents.ability.generic.ActiveAbility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbilityHandler {

    private final List<ActiveAbility> activeAbilities = new ArrayList<>();

    public void addAbility(ActiveAbility ability, AbilityContext ctx) {

        ActiveAbility existing = getAbility(ability.getClass());

        if (existing == null) {
            ability.start(ctx);
            activeAbilities.add(ability);
            return;
        }

        if (ability.isTogglable()) {
            existing.end();
            activeAbilities.remove(existing);
            return;
        }

        existing.refresh(ability);
    }

    public <T extends ActiveAbility> T getAbility(Class<T> type) {
        for (ActiveAbility ability : activeAbilities) {
            if (type.isInstance(ability)) {
                return type.cast(ability);
            }
        }
        return null;
    }

    public boolean hasAbility(Class<? extends ActiveAbility> type) {
        return activeAbilities.stream().anyMatch(type::isInstance);
    }

    public void tick() {

        Iterator<ActiveAbility> iterator = activeAbilities.iterator();

        while (iterator.hasNext()) {

            ActiveAbility ability = iterator.next();

            ability.tick();

            if (ability.isEnded() || ability.shouldEnd()) {
                ability.end();
                iterator.remove();
            }
        }
    }
}