package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.gui.ItemSelectionMenu;
import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.contents.gui.UpgradeMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class CSCMenuTypes {
    public static final MenuType<ShopMenu> SHOP_MENU = register("shop", ShopMenu::new);
    public static final MenuType<ItemSelectionMenu> ITEM_SELECTION_MENU = register("item_selection_menu", ItemSelectionMenu::new);
    public static final MenuType<UpgradeMenu> UPGRADE_MENU = register("upgrade_menu", UpgradeMenu::new);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Menu Types");
    }

    public static <T extends AbstractContainerMenu> MenuType<T> register(
            String name,
            MenuType.MenuSupplier<T> constructor
    ) {
        Identifier key = Identifier.fromNamespaceAndPath(CSC.MOD_ID, name);
        return Registry.register(BuiltInRegistries.MENU, key, new MenuType<>(constructor, FeatureFlagSet.of()));
    }
}
