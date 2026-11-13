package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.command.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CSCCommands {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Commands");

        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, context, _) -> {
                    SetGold.register(dispatcher);
                    SetCharacter.register(dispatcher, context);
                    OpenItemSelection.register(dispatcher);
                    ScaleEntity.register(dispatcher);
                    GameState.register(dispatcher);
                }
        );
    }
}
