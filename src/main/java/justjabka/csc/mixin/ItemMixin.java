package justjabka.csc.mixin;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"))
    public void triggerAbilityOnUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item = player.getItemInHand(hand);

        AbilityComponent ability = item.get(CSCComponents.ABILITY);
        if (ability == null) return;

        if (!ability.activationTypes().contains(ActivationType.GENERIC)) return;
        ability.onUse(level, player, hand);
    }
}
