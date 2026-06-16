package justjabka.csc.contents.block_entity;

import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.registries.CSCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShopBlockEntity extends BlockEntity implements MenuProvider {
    public ShopBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.SHOP_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.shop");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ShopMenu(containerId, playerInventory);
    }
}
