package justjabka.csc.registries.client;

import justjabka.csc.CSC;
import justjabka.csc.contents.screen.ItemSelectionScreen;
import justjabka.csc.contents.screen.ShopScreen;
import justjabka.csc.registries.CSCMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class CSCScreens {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Screens");
        registerScreens();
    }

    private static void registerScreens() {
        MenuScreens.register(CSCMenuTypes.SHOP_MENU, ShopScreen::new);
        MenuScreens.register(CSCMenuTypes.ITEM_SELECTION_MENU, ItemSelectionScreen::new);
    }
}
