package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public class SetGold {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setGold")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            int amount = IntegerArgumentType.getInteger(context, "amount");
                            return setGold(context.getSource(), player, amount);
                        })
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    int amount = IntegerArgumentType.getInteger(context, "amount");
                                    return setGold(context.getSource(), player, amount);
                                })
                        )
                )
        );
    }

    private static int setGold(CommandSourceStack source, ServerPlayer player, int gold) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        data.setGold(gold);
        source.sendSuccess(() -> Component.translatable("message.csc.command.setGold",
                data.getGold(),
                player.getDisplayName()
        ), true);
        return Command.SINGLE_SUCCESS;
    }
}
