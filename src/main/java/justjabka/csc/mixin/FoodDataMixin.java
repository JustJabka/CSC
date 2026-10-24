package justjabka.csc.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void disableHunger(float amount, CallbackInfo ci) {
        ci.cancel();
    }
}
