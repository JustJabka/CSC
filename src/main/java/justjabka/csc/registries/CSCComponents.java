package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.component.UpgradableComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.function.UnaryOperator;

public class CSCComponents {
    public static final DataComponentType<ShopItemComponent> SHOP_ITEM = register("shop_item",
            builder -> builder.persistent(ShopItemComponent.CODEC)
    );

    public static final DataComponentType<UpgradableComponent> UPGRADABLE = register("upgradable",
            builder -> builder.persistent(UpgradableComponent.CODEC)
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Components");
    }

    private static <T> DataComponentType<T> register(final String id, final UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(CSC.MOD_ID, id),
                builder.apply(DataComponentType.builder()).build()
        );
    }
}
