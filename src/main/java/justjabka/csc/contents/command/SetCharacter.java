package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import justjabka.csc.contents.command.argument.CharacterArgumentType;
import justjabka.csc.handlers.CharacterHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public class SetCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setcharacter")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("character", CharacterArgumentType.character())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Identifier id = CharacterArgumentType.getCharacter(context, "character");

                            return setCharacter(context.getSource(), player, id);
                        })
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Identifier id = CharacterArgumentType.getCharacter(context, "character");

                                    return setCharacter(context.getSource(), player, id);
                                })
                        )
                )
        );
    }

    private static int setCharacter(CommandSourceStack source, ServerPlayer player, Identifier character) {
        CharacterHandler.setCharacter(player, character);

        String characterDisplayName = character.toString();

        source.sendSuccess(() -> Component.translatable("message.csc.command.setcharacter",
                characterDisplayName,
                player.getDisplayName()
        ), true);
        return Command.SINGLE_SUCCESS;
    }
}
