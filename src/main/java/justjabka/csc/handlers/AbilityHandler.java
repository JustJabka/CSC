package justjabka.csc.handlers;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.contents.ability.AbilityInstance;
import justjabka.csc.contents.ability.TogglableAbility;
import justjabka.csc.contents.attachement.AbilityData;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class AbilityHandler {

    private AbilityHandler() {}

    private static AbilityData getActiveAbilities(Player player) {
        return player.getAttachedOrCreate(CSCAttachments.ABILITY_DATA);
    }

    public static void startAbility(Player player, @Nullable AbilityInstance ability, AbilityContext ctx) {
        if (ability == null) return;

        AbilityData abilities = getActiveAbilities(player);

        Holder<Ability> type = ability.getAbility();
        AbilityInstance existing = abilities.abilities().get(type);

        if (existing == null) {
            ability.start(ctx);
            player.setAttached(CSCAttachments.ABILITY_DATA, abilities.addAbility(ability));
            return;
        }

        if (type.value() instanceof TogglableAbility) {
            existing.end();
            player.setAttached(CSCAttachments.ABILITY_DATA, abilities.removeAbility(ability));
            return;
        }

        existing.update(ability);
    }

    public static void stopAbility(Player player, @Nullable AbilityInstance ability) {
        if (ability == null) return;
        stopAbility(player, ability.getAbility());
    }

    public static void stopAbility(Player player, @Nullable Holder<Ability> type) {
        if (type == null) return;

        AbilityData abilities = getActiveAbilities(player);

        AbilityInstance existing = abilities.abilities().get(type);

        if (existing == null) return;

        existing.end();
        player.setAttached(CSCAttachments.ABILITY_DATA, abilities.removeAbility(type));
    }

    public static @Nullable AbilityInstance getAbility(Player player, @Nullable Holder<Ability> type) {
        if (type == null) return null;
        return getActiveAbilities(player).abilities().get(type);
    }

    public static void tick(ServerPlayer player) {
        AbilityData data = getActiveAbilities(player);
        Map<Holder<Ability>, AbilityInstance> abilities = data.abilities();

        if (abilities.isEmpty()) return;

        Map<Holder<Ability>, AbilityInstance> newAbilities = new HashMap<>(abilities);

        var iterator = newAbilities.entrySet().iterator();
        while (iterator.hasNext()) {
            AbilityInstance ability = iterator.next().getValue();

            if (ability.tick()) {
                newAbilities.put(ability.getAbility(), new AbilityInstance(ability));
                continue;
            }

            ability.end();
            iterator.remove();
        }

        player.setAttached(CSCAttachments.ABILITY_DATA, new AbilityData(newAbilities));
    }
}