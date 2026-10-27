package justjabka.csc.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCraftingMenu.class)
public abstract class AbstractCraftingMenuMixin extends RecipeBookMenu {

    @Shadow
    @Final
    protected CraftingContainer craftSlots;

    @Shadow
    @Final
    protected ResultContainer resultSlots;

    @Shadow
    @Final
    private int width;

    @Shadow
    @Final
    private int height;

    public AbstractCraftingMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "addResultSlot", at = @At("HEAD"), cancellable = true)
    private void removeResultSlot(Player player, int x, int y, CallbackInfoReturnable<Slot> cir) {
        Slot slot = this.addSlot(new ResultSlot(player, this.craftSlots, this.resultSlots, 0, x, y) {
            @Override
            public boolean isHighlightable() {
                return false;
            }
        });

        cir.setReturnValue(slot);
    }

    @Inject(method = "addCraftingGridSlots", at = @At("HEAD"), cancellable = true)
    private void removeCraftingGridSlots(int left, int top, CallbackInfo ci) {
        for (int y = 0; y < this.width; y++) {
            for (int x = 0; x < this.height; x++) {
                this.addSlot(new Slot(this.craftSlots, x + y * this.width, left + x * 18, top + y * 18) {
                    @Override
                    public boolean mayPlace(@NonNull ItemStack itemStack) {
                        return false;
                    }

                    @Override
                    public boolean isHighlightable() {
                        return false;
                    }
                });
            }
        }

        ci.cancel();
    }
}
