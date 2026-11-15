package justjabka.csc.registries;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.contents.character.Character;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;

public class CSCBuiltInRegistries {
    public static final Registry<Ability> ABILITY = FabricRegistryBuilder.create(CSCRegistries.ABILITY)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
    public static final Registry<Character> CHARACTER = FabricRegistryBuilder.create(CSCRegistries.CHARACTER)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();
}
