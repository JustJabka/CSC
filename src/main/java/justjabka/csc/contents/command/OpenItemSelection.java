package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import justjabka.csc.handlers.ItemSelectionHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public class OpenItemSelection {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("openitemselection")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            try {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                ItemSelectionHandler.openMenu(player);
                                return Command.SINGLE_SUCCESS;
                            } catch (Exception e) {
                                context.getSource().sendFailure(Component.literal("Краш коду: " + e.getMessage()));
                                e.printStackTrace(); // 🔥 Ось це виведе стек-трейс в консоль, попри Brigadier!
                                return 0;
                            }
                        })
                )
        );
    }
}
