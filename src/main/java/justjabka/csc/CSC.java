package justjabka.csc;

import com.google.common.eventbus.Subscribe;
import com.mojang.brigadier.CommandDispatcher;
import justjabka.csc.contents.command.SetGold;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCItemGroups;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSC implements ModInitializer {
	public static final String MOD_ID = "csc";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CSCItems.initialize();
		CSCItemGroups.initialize();
		CSCSounds.initialize();
		CSCAttachments.initialize();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SetGold.register(dispatcher);
		});
	}
}