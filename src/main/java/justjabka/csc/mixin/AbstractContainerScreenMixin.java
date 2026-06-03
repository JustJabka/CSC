package justjabka.csc.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(
            method = "slotClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSlotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci) {
        boolean clickedOutside = slotId == AbstractContainerMenu.SLOT_CLICKED_OUTSIDE;
        boolean inputThrow = containerInput == ContainerInput.THROW;
        boolean isDrop = clickedOutside || inputThrow;

        if (isDrop) {
            ci.cancel();
        }
    }
}
