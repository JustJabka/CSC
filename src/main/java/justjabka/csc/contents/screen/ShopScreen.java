package justjabka.csc.contents.screen;

import justjabka.csc.CSC;
import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.payloads.ShopSyncContentPayload;
import justjabka.csc.types.ShopCategory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {
    private static final Identifier CONTAINER_TEXTURE = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "textures/gui/container/shop.png");

    private static final Identifier[] UNSELECTED_TOP_TABS = new Identifier[]{
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_1"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_3"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_4"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_5"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_6"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_7")
    };
    private static final Identifier[] SELECTED_TOP_TABS = new Identifier[]{
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_1"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_2"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_3"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_4"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_5"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_6"),
            Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_7")
    };

    private static final int TAB_WIDTH = 26;
    private static final int TAB_HEIGHT = 28;
    private static final int TAB_GAP = 1;

    private EditBox searchBox;

    public int getTabX(int index) {
        return this.leftPos + 4 + (index * (TAB_WIDTH + TAB_GAP));
    }

    public int getTabY() {
        return this.topPos - 28;
    }

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

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractBackground(graphics, mouseX, mouseY, delta);

        renderTabBackgrounds(graphics, false);
        renderTabBackgrounds(graphics, true);

        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractContents(graphics, mouseX, mouseY, a);
        this.extractCarriedItem(graphics, mouseX, mouseY);
        this.extractSnapbackItem(graphics);

        renderTabIcons(graphics);

        this.extractTooltip(graphics, mouseX, mouseY);
    }

    private void initSearchBox() {
        this.searchBox = new EditBox(this.font, this.leftPos + 82, this.topPos + 6, 80, 9, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(-1);
        this.searchBox.setInvertHighlightedTextColor(false);

        this.searchBox.setResponder(query -> ClientPlayNetworking.send(new ShopSyncContentPayload(query)));

        this.addRenderableWidget(this.searchBox);
    }

    private void initTopTabs() {
        ShopCategory[] categories = ShopCategory.values();

        for (int i = 0; i < categories.length; i++) {
            ShopCategory category = categories[i];

            this.addWidget(Button.builder(Component.empty(), onTabPress(category))
                    .bounds(getTabX(i), getTabY(), TAB_WIDTH, TAB_HEIGHT)
                    .build()
            );
        }
    }

    private Button.@NonNull OnPress onTabPress(ShopCategory category) {
        return _ -> {
            MultiPlayerGameMode gameMode = this.minecraft.gameMode;

            if (gameMode == null) return;
            gameMode.handleInventoryButtonClick(this.menu.containerId, category.ordinal());
        };
    }

    private void renderTabBackgrounds(GuiGraphicsExtractor graphics, boolean drawSelected) {
        ShopCategory[] categories = ShopCategory.values();

        for (int i = 0; i < categories.length; i++) {
            ShopCategory category = categories[i];
            boolean isCurrent = (category == this.menu.getCurrentCategory());

            if (isCurrent != drawSelected) continue;

            Identifier[] sprites = isCurrent ? SELECTED_TOP_TABS : UNSELECTED_TOP_TABS;
            Identifier sprite = sprites[Mth.clamp(i, 0, sprites.length - 1)];

            int currentHeight = isCurrent ? 32 : 28;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getTabX(i), getTabY(), TAB_WIDTH, currentHeight);
        }
    }

    private void renderTabIcons(GuiGraphicsExtractor graphics) {
        ShopCategory[] categories = ShopCategory.values();

        for (int i = 0; i < categories.length; i++) {
            ShopCategory category = categories[i];

            boolean isCurrent = (category == this.menu.getCurrentCategory());

            int iconX = getTabX(i) + 13 - 8;
            int iconY =  getTabY() + 16 - 8 + (isCurrent ? 1 : 2);

            graphics.item(category.getIcon(), iconX, iconY);
        }
    }
}
