package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.BerserkCharacter;
import justjabka.csc.contents.character.SwordsmanCharacter;
import justjabka.csc.contents.character.TitanCharacter;
import justjabka.csc.contents.character.generic.BaseCharacter;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CSCCharacters {
    private static final Map<Identifier, BaseCharacter> CHARACTERS = new HashMap<>();

    public static void initialize() {
        CSC.LOGGER.info("Initializing Characters");

        registerCharacter(new SwordsmanCharacter());
        registerCharacter(new TitanCharacter());
        registerCharacter(new BerserkCharacter());
    }

    public static void registerCharacter(BaseCharacter character) {
        CHARACTERS.put(character.getKey(), character);
    }

    public static Map<Identifier, BaseCharacter> getCharacters() {
        return Collections.unmodifiableMap(CHARACTERS);
    }

    public static BaseCharacter getByKey(Identifier key) {
        return CHARACTERS.get(key);
    }
}
