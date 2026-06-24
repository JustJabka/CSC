package justjabka.csc;

import justjabka.csc.registries.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSC implements ModInitializer {
	public static final String MOD_ID = "csc";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CSCComponents.initialize();

		CSCItems.initialize();
		CSCItemGroups.initialize();
		CSCSounds.initialize();
		CSCAttachments.initialize();
		CSCAttributes.initialize();
		CSCPayloads.initialize();
		CSCCharacters.initialize();
		CSCEvents.initialize();
		CSCCommands.initialize();
		CSCBlocks.initialize();
		CSCMenuTypes.initialize();
		CSCLootTables.initialize();
		CSCTooltipProviders.initialize();
		CSCAbilities.initialize();
	}
}