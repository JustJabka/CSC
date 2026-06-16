package justjabka.csc.contents.screen;

import justjabka.csc.CSC;
import justjabka.csc.contents.gui.ShopMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {
    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "textures/gui/container/shop.png");

    public ShopScreen(ShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 204);
        this.inventoryLabelY = 110;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
