package justjabka.csc.handlers;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbilityHandler {
    private final List<BaseActiveAbility> ACTIVE_ABILITIES = new ArrayList<>();

    public void addAbility(BaseActiveAbility ability) {
        if (ability == null) return;

        BaseActiveAbility existing = getAbility(ability.getClass());

        if (existing == null) {
            ability.start();
            ACTIVE_ABILITIES.add(ability);
            return;
        }

        if (ability instanceof BaseTogglableActiveAbility) {
            existing.end();
            ACTIVE_ABILITIES.remove(existing);
            return;
        }

        existing.refresh(ability);
    }

    public <T extends BaseActiveAbility> T getAbility(Class<T> type) {
        for (BaseActiveAbility ability : ACTIVE_ABILITIES) {
            if (type.isInstance(ability)) {
                return type.cast(ability);
            }
        }
        return null;
    }

    public boolean hasAbility(Class<? extends BaseActiveAbility> type) {
        return ACTIVE_ABILITIES.stream().anyMatch(type::isInstance);
    }

    public List<BaseActiveAbility> getActiveAbilities() {
        return ACTIVE_ABILITIES;
    }

    public void tick() {
        Iterator<BaseActiveAbility> iterator = ACTIVE_ABILITIES.iterator();

        while (iterator.hasNext()) {
            BaseActiveAbility ability = iterator.next();

            if (!ability.isPlayerValid()) {
                ability.end();
                iterator.remove();
                continue;
            }

            ability.tick();

            boolean isEnded = ability.isEnded() || ability.shouldEnd();
            if (isEnded) {
                ability.end();
                iterator.remove();
            }
        }
    }

    public void stopAbility(Class<? extends BaseActiveAbility> type) {
        for (BaseActiveAbility ability : ACTIVE_ABILITIES) {
            if (!type.isInstance(ability)) continue;
            ability.forceEnd();
        }
    }
}