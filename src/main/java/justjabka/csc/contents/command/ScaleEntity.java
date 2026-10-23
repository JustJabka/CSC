package justjabka.csc.contents.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import justjabka.csc.handlers.WaveHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ScaleEntity {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("scaleentity")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.argument("entity", EntityArgument.entity())
                        .then(Commands.argument("wave", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    final Entity entity = EntityArgument.getEntity(context, "entity");
                                    final int wave = IntegerArgumentType.getInteger(context, "wave");

                                    if (!(entity instanceof LivingEntity livingEntity)) return 0;

                                    WaveHandler.scaleEntityAttributes(livingEntity, wave);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
