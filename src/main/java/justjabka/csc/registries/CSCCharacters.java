package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.BerserkCharacter;
import justjabka.csc.contents.character.SwordsmanCharacter;
import justjabka.csc.contents.character.TitanCharacter;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.events.OnPlayerHealthChangeCallback;
import justjabka.csc.handlers.CharacterHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static justjabka.csc.handlers.CharacterHandler.isCurrentCharacter;

public class CSCCharacters {
    private static final Map<Identifier, BaseCharacter> CHARACTERS = new HashMap<>();

    public static final SwordsmanCharacter SWORDSMAN = new SwordsmanCharacter();
    public static final TitanCharacter TITAN = new TitanCharacter();
    public static final BerserkCharacter BERSERK = new BerserkCharacter();

    public static void initialize() {
        CSC.LOGGER.info("Initializing Characters");

        registerCharacter(SWORDSMAN);
        registerCharacter(TITAN);
        registerCharacter(BERSERK);
    }

    public static void registerCharacter(BaseCharacter character) {
        CHARACTERS.put(character.getKey(), character);
        registerCharactersEvents(character);
    }

    public static Map<Identifier, BaseCharacter> getCharacters() {
        return Collections.unmodifiableMap(CHARACTERS);
    }

    public static BaseCharacter getByKey(Identifier key) {
        return CHARACTERS.get(key);
    }

    private static void registerCharactersEvents(BaseCharacter character) {
        if (character instanceof OnPlayerHealthChangeCallback listener) {
            OnPlayerHealthChangeCallback.EVENT.register((player, oldHealth, newHealth) -> {
                if (!isCurrentCharacter(player, character.getKey())) return;
                listener.onChange(player, oldHealth, newHealth);
            });
        }

        if (character instanceof ServerLivingEntityEvents.AllowDeath listener) {
            ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
                if (!(entity instanceof Player player)) return true;
                if (!CharacterHandler.isCurrentCharacter(player, character.getKey())) return true;

                return listener.allowDeath(player, damageSource, damageAmount);
            });
        }
    }
}
