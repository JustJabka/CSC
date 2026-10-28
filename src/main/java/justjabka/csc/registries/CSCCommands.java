package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.command.*;
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
                CSC.key("character"),
                CharacterArgumentType.class,
                SingletonArgumentInfo.contextFree(CharacterArgumentType::new)
        );
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, _, _) -> {
                    SetGold.register(dispatcher);
                    SetCharacter.register(dispatcher);
                    OpenItemSelection.register(dispatcher);
                    ScaleEntity.register(dispatcher);
                    GameState.register(dispatcher);
                }
        );
    }
}
