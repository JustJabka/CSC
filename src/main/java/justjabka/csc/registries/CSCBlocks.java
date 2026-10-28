package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.block.ShopBlock;
import justjabka.csc.contents.block.UpgradeBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
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
            CSCBlockItemIds.SHOP,
            ShopBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD)
    );
    public static final Block UPGRADE = register(
            CSCBlockItemIds.UPGRADE,
            UpgradeBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.IRON)
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Blocks");
    }

    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        // Create the block instance
        Block block = register(id.block(), blockFactory, properties);

        // Create the block item instance
        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
        CREATIVE_TAB_BLOCKS.add(block);

        return block;
    }
}
