package justjabka.csc.handlers;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.*;


public class AbilityHandler {
    private static final Map<Player, List<BaseActiveAbility>> ACTIVE_ABILITIES = new WeakHashMap<>();

    public static void addAbility(Player player, BaseActiveAbility ability) {
        if (ability == null) return;

        List<BaseActiveAbility> playerAbilities = ACTIVE_ABILITIES.computeIfAbsent(player, plr -> new ArrayList<>());
        BaseActiveAbility existing = getAbilityInstance(player, ability.getClass());

        if (existing == null) {
            ability.start();
            playerAbilities.add(ability);
            return;
        }

        if (ability instanceof BaseTogglableActiveAbility) {
            existing.end();
            playerAbilities.remove(existing);
            return;
        }

        existing.refresh(ability);
    }

    public static <T extends BaseActiveAbility> T getAbilityInstance(Player player, Class<T> type) {
        List<BaseActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return null;

        for (BaseActiveAbility ability : abilities) {
            if (!type.isInstance(ability)) continue;
            return type.cast(ability);
        }

        return null;
    }

    public static @Nullable List<BaseActiveAbility> getPlayerAbilities(Player player) {
        if (ACTIVE_ABILITIES.isEmpty()) return null;
        if (!ACTIVE_ABILITIES.containsKey(player)) return null;

        return ACTIVE_ABILITIES.get(player);
    }

    public static void tick(Player player) {
        List<BaseActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return;

        Iterator<BaseActiveAbility> iterator = abilities.iterator();

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

        if (abilities.isEmpty()) {
            ACTIVE_ABILITIES.remove(player);
        }
    }

    public static void stopAbility(Player player, Class<? extends BaseActiveAbility> type) {
        List<BaseActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return;

        for (BaseActiveAbility ability : abilities) {
            if (!type.isInstance(ability)) continue;
            ability.forceEnd();
        }
    }
}