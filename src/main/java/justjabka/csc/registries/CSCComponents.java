package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.component.ShopItemComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class CSCComponents {
    public static final DataComponentType<ShopItemComponent> SHOP_ITEM = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(CSC.MOD_ID, "shop_item"),
            DataComponentType.<ShopItemComponent>builder().persistent(ShopItemComponent.CODEC).build()
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Components");
    }
}
