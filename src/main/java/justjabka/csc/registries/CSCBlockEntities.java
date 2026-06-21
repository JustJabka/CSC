package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.block_entity.ShopBlockEntity;
import justjabka.csc.contents.block_entity.UpgradeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CSCBlockEntities {
    public static final BlockEntityType<ShopBlockEntity> SHOP_BLOCK_ENTITY =
            register("shop", ShopBlockEntity::new, CSCBlocks.SHOP);

    public static final BlockEntityType<UpgradeBlockEntity> UPGRADE_BLOCK_ENTITY =
            register("upgrade", UpgradeBlockEntity::new, CSCBlocks.UPGRADE);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Block Entities");
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.fromNamespaceAndPath(CSC.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
