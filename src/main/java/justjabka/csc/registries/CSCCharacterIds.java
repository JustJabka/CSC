package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.Character;
import net.minecraft.resources.ResourceKey;

public class CSCCharacterIds {

    public static final ResourceKey<Character> NONE = create("none");

    public static final ResourceKey<Character> SWORDSMAN = create("swordsman");
    public static final ResourceKey<Character> TITAN = create("titan");
    public static final ResourceKey<Character> BERSERK = create("berserk");

    public static ResourceKey<Character> create(String name) {
        // Create the item key.
        return ResourceKey.create(CSCRegistries.CHARACTER, CSC.id(name));
    }
}
