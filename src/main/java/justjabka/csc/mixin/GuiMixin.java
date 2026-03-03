package justjabka.csc.mixin;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @ModifyVariable(
            method = "renderSelectedItemName",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            name = "k"
    )
    private int moveTooltip(int k) {
        return k - 10;
    }
}