package justjabka.csc;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.command.SetGold;
import justjabka.csc.registries.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
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
		CSCAttributes.initialize();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> SetGold.register(dispatcher));

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
				data.getAbilityHandler().tick();
			}
		});
	}
}