package justjabka.csc.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Redirect(
            method = "handleKeybinds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    )
    private boolean onConsumeKeybind(KeyMapping instance) {
        Minecraft client = (Minecraft) (Object) this;

        if (instance == client.options.keyDrop) {
            instance.consumeClick();
            return false;
        }

        return instance.consumeClick();
    }
}
