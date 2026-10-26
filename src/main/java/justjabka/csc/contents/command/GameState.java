package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import justjabka.csc.handlers.GameStateHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permissions;

public class GameState {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gamestate")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.literal("set")
                        .then(Commands.literal("hub")
                                .executes(context -> {
                                    MinecraftServer server = context.getSource().getServer();

                                    GameStateHandler.startHub(server);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
