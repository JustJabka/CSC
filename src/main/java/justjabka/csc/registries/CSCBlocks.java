package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.block.ShopBlock;
import justjabka.csc.contents.block.UpgradeBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CSCBlocks {
    public static final List<Block> CREATIVE_TAB_BLOCKS = new ArrayList<>();

    public static final Block SHOP = register(
            "shop",
            ShopBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD),
            true
    );
    public static final Block UPGRADE = register(
            "upgrade",
            UpgradeBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.IRON),
            true
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Blocks");
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());

            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
            CREATIVE_TAB_BLOCKS.add(block);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, CSC.id(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, CSC.id(name));
    }
}
