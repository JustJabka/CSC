package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.Character;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class CSCRegistries {
    public static final ResourceKey<Registry<Character>> CHARACTER = createRegistryKey("character");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name) {
        return ResourceKey.createRegistryKey(CSC.id(name));
    }
}
