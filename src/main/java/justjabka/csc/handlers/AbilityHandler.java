package justjabka.csc.handlers;

import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.contents.ability.generic.TogglableActiveAbility;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

import java.util.*;


public final class AbilityHandler {

    private AbilityHandler() {}

    private static final Map<Player, List<ActiveAbility>> ACTIVE_ABILITIES = new WeakHashMap<>();

    public static void addAbility(Player player, @Nullable ActiveAbility ability) {
        if (ability == null) return;

        List<ActiveAbility> playerAbilities = ACTIVE_ABILITIES.computeIfAbsent(player, _ -> new ArrayList<>());
        ActiveAbility existing = getAbilityInstance(player, ability.getClass());

        if (existing == null) {
            ability.start();
            playerAbilities.add(ability);
            return;
        }

        if (ability instanceof TogglableActiveAbility) {
            existing.end();
            playerAbilities.remove(existing);
            return;
        }

        existing.refresh(ability);
    }

    public static <T extends ActiveAbility> T getAbilityInstance(Player player, Class<T> type) {
        List<ActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return null;

        for (ActiveAbility ability : abilities) {
            if (!type.isInstance(ability)) continue;
            return type.cast(ability);
        }

        return null;
    }

    public static @Nullable List<ActiveAbility> getPlayerAbilities(Player player) {
        if (ACTIVE_ABILITIES.isEmpty()) return null;
        if (!ACTIVE_ABILITIES.containsKey(player)) return null;

        return ACTIVE_ABILITIES.get(player);
    }

    public static void tick(Player player) {
        List<ActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return;

        Iterator<ActiveAbility> iterator = abilities.iterator();

        while (iterator.hasNext()) {
            ActiveAbility ability = iterator.next();

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

    public static void stopAbility(Player player, Class<? extends ActiveAbility> type) {
        List<ActiveAbility> abilities = getPlayerAbilities(player);
        if (abilities == null) return;

        for (ActiveAbility ability : abilities) {
            if (!type.isInstance(ability)) continue;
            ability.forceEnd();
        }
    }
}