package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import justjabka.csc.contents.character.Character;
import justjabka.csc.handlers.CharacterHandler;
import justjabka.csc.registries.CSCRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import org.jspecify.annotations.NonNull;

public class SetCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext context) {
        dispatcher.register(Commands.literal("setcharacter")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("character", ResourceArgument.resource(context, CSCRegistries.CHARACTER))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();

                            return setCharacter(ctx.getSource(), player, getCharacter(ctx));
                        })
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");

                                    return setCharacter(ctx.getSource(), player, getCharacter(ctx));
                                })
                        )
                )
        );
    }

    private static int setCharacter(CommandSourceStack source, ServerPlayer player, Holder.Reference<Character> characterHolder) {
        Character character = characterHolder.value();

        CharacterHandler.setCharacter(player, character);

        source.sendSuccess(() -> Component.translatable("message.csc.command.setcharacter",
                character.getDisplayName(),
                player.getDisplayName()
        ), true);
        return Command.SINGLE_SUCCESS;
    }

    private static Holder.@NonNull Reference<Character> getCharacter(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return ResourceArgument.getResource(ctx, "character", CSCRegistries.CHARACTER);
    }
}
