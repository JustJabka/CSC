package justjabka.csc.contents.block;

import com.mojang.serialization.MapCodec;
import justjabka.csc.contents.gui.UpgradeMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UpgradeBlock extends Block {
    public static final MapCodec<UpgradeBlock> CODEC = simpleCodec(UpgradeBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.upgrade");

    public UpgradeBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new UpgradeMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)),
                CONTAINER_TITLE
        );
    }
}