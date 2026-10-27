package justjabka.csc.mixin;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.handlers.CharacterHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(Player player, CallbackInfo ci) {
        this.title = Component.translatable("container.character");

        // TODO: fix pos
        this.addRenderableWidget(new ImageButton(this.leftPos + 98, this.height / 2 - 18, 32, 32, getCharacterSprite(player), _ -> {}));
    }

    @Unique
    private WidgetSprites getCharacterSprite(Player player) {
        BaseCharacter character = CharacterHandler.getCharacter(player);

        Identifier icon = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "character/missingno");
        if (character != null) {
            icon = character.getDisplayIcon();
        }

        return new WidgetSprites(icon);
    }
}
