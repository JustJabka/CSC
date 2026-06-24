package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;

public class CSCTooltipProviders {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Tooltip Providers");

        ItemComponentTooltipProviderRegistry.addFirst(CSCComponents.SHOP_ITEM);
        ItemComponentTooltipProviderRegistry.addAfter(CSCComponents.SHOP_ITEM, CSCComponents.ABILITY);
        ItemComponentTooltipProviderRegistry.addFirst(CSCComponents.UPGRADABLE);
    }
}
