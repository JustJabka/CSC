package justjabka.csc.mixin;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.handlers.CharacterHandler;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
        super(menu, recipeBookComponent, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.title = Component.translatable("container.character");

        ScreenPosition pos = new ScreenPosition(this.leftPos + 106, this.height / 2 - 64);

        this.addRenderableWidget(new ImageButton(pos.x(), pos.y(), 32, 32, getCharacterSprite(), _ -> {}));
    }

    @Unique
    private WidgetSprites getCharacterSprite() {
        BaseCharacter character = CharacterHandler.getClientCharacter();

        Identifier icon = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "character/missingno");
        if (character != null) {
            icon = character.getDisplayIcon();
        }

        return new WidgetSprites(icon);
    }
}
