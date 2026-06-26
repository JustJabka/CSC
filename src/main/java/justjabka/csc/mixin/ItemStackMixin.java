package justjabka.csc.mixin;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void triggerAbilityOnInteractionUse(Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        AbilityComponent ability = stack.get(CSCComponents.ABILITY);
        if (ability == null) return;

        if (!ability.activationTypes().contains(ActivationType.INTERACTION)) return;

        InteractionResult result = ability.onInteractionUse(stack, player, target, hand);

        if (result != InteractionResult.PASS) {
            cir.setReturnValue(result);
        }
    }
}
