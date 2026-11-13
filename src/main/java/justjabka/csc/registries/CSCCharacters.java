package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.*;
import justjabka.csc.contents.character.Character;
import justjabka.csc.events.OnPlayerHealthChangeCallback;
import justjabka.csc.handlers.CharacterHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

@SuppressWarnings("unused")
public class CSCCharacters {
    public static final Character NONE = register(CSCCharacterIds.NONE, NoneCharacter::new);

    public static final Character SWORDSMAN = register(CSCCharacterIds.SWORDSMAN, SwordsmanCharacter::new);
    public static final Character TITAN = register(CSCCharacterIds.TITAN, TitanCharacter::new);
    public static final Character BERSERK = register(CSCCharacterIds.BERSERK, BerserkCharacter::new);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Characters");
    }

    public static Character register(ResourceKey<Character> characterKey, Function<Character.Properties, Character> characterFactory) {
        return register(characterKey, characterFactory, new Character.Properties());
    }

    public static Character register(ResourceKey<Character> characterKey, Function<Character.Properties, Character> characterFactory, Character.Properties settings) {
        Character character = characterFactory.apply(settings.setId(characterKey));

        Registry.register(CSCBuiltInRegistries.CHARACTER, characterKey, character);

//        registerCharactersEvents(character);
        return character;
    }

    // TODO: replace character events with separate passive abilities
    private static void registerCharactersEvents(Character character) {
        if (character instanceof OnPlayerHealthChangeCallback listener) {
            OnPlayerHealthChangeCallback.EVENT.register((player, oldHealth, newHealth) -> {
                if (!CharacterHandler.isCurrentCharacter(player, character)) return;
                listener.onChange(player, oldHealth, newHealth);
            });
        }

        if (character instanceof ServerLivingEntityEvents.AllowDeath listener) {
            ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
                if (!(entity instanceof Player player)) return true;
                if (!CharacterHandler.isCurrentCharacter(player, character)) return true;

                return listener.allowDeath(player, damageSource, damageAmount);
            });
        }
    }
}
