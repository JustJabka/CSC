package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.command.OpenItemSelection;
import justjabka.csc.contents.command.SetCharacter;
import justjabka.csc.contents.command.SetGold;
import justjabka.csc.contents.command.argument.CharacterArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.Identifier;

public class CSCCommands {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Commands");

        registerArgumentTypes();
        registerCommands();
    }

    private static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.fromNamespaceAndPath(CSC.MOD_ID, "character"),
                CharacterArgumentType.class,
                SingletonArgumentInfo.contextFree(CharacterArgumentType::new)
        );
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    SetGold.register(dispatcher);
                    SetCharacter.register(dispatcher);
                    OpenItemSelection.register(dispatcher);
                }
        );
    }
}
