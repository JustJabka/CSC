package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CSCItemGroups {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Item Groups");
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CSC_CREATIVE_TAB_KEY, CSC_CREATIVE_TAB);
    }

    public static final ResourceKey<CreativeModeTab> CSC_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(CSC.MOD_ID, "creative_tab"));
    public static final CreativeModeTab CSC_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(CSCItems.MIDAS))
            .title(Component.translatable("itemGroup.csc"))
            .displayItems((params, output) -> {
                output.accept(CSCItems.MIDAS);
                output.accept(CSCItems.PHOENIX_FEATHER);
            })
            .build();
}
