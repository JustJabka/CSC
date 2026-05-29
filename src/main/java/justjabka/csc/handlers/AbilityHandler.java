package justjabka.csc.handlers;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbilityHandler {
    private final List<BaseActiveAbility> activeAbilities = new ArrayList<>();

    public void addAbility(BaseActiveAbility ability, AbilityContext ctx) {
        if (ability == null) return;

        BaseActiveAbility existing = getAbility(ability.getClass());

        if (existing == null) {
            ability.start(ctx);
            activeAbilities.add(ability);
            return;
        }

        if (ability instanceof BaseTogglableActiveAbility) {
            existing.end();
            activeAbilities.remove(existing);
            return;
        }

        existing.refresh(ability);
    }

    public <T extends BaseActiveAbility> T getAbility(Class<T> type) {
        for (BaseActiveAbility ability : activeAbilities) {
            if (type.isInstance(ability)) {
                return type.cast(ability);
            }
        }
        return null;
    }

    public boolean hasAbility(Class<? extends BaseActiveAbility> type) {
        return activeAbilities.stream().anyMatch(type::isInstance);
    }

    public List<BaseActiveAbility> getActiveAbilities() {
        return activeAbilities;
    }

    public void tick() {
        Iterator<BaseActiveAbility> iterator = activeAbilities.iterator();

        while (iterator.hasNext()) {
            BaseActiveAbility ability = iterator.next();

            ability.tick();

            if (ability.isEnded() || ability.shouldEnd()) {
                ability.end();
                iterator.remove();
            }
        }
    }
}