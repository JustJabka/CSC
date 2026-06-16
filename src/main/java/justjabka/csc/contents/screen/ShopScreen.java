package justjabka.csc.contents.screen;

import justjabka.csc.CSC;
import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.types.ShopCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {
    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "textures/gui/container/shop.png");

    private EditBox searchBox;
    public ShopScreen(ShopMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
//        this.inventoryLabelY = this.height - 94;

        initSearchBox();
        initTopTabs();
    }

    private void initTopTabs() {
        int tabWidth = 26;
        int tabHeight = 32;
        int gap = 2;

        ShopCategory[] categories = ShopCategory.values();

        int startX = this.leftPos + 8;
        int tabY = this.topPos - tabHeight;

        for (int i = 0; i < categories.length; i++) {
            ShopCategory category = categories[i];

            Component tabName = category.getDisplayName();

            this.addRenderableWidget(Button.builder(tabName, button -> {
                        if (this.minecraft.gameMode != null) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, category.ordinal());
                        }
                    })
                    .bounds(startX + (i * (tabWidth + gap)), tabY, tabWidth, tabHeight)
                    .build());
        }
    }

    private void initSearchBox() {
        this.searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(-1);
        this.searchBox.setInvertHighlightedTextColor(false);

        this.searchBox.setResponder(this.menu::changeSearchQuery);

        this.addRenderableWidget(this.searchBox);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }
}
