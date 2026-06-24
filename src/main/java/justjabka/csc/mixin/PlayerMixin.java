package justjabka.csc.mixin;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue()
                .add(CSCAttributes.DODGE_CHANCE)
                .add(CSCAttributes.DAMAGE_REFLECTION_PERCENT)
                .add(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER)
                .add(CSCAttributes.MAGIC_RESISTANCE)
                .add(CSCAttributes.MAGIC_DAMAGE)
                .add(CSCAttributes.DAMAGE_BOOK_BONUS)
                .add(CSCAttributes.HEALTH_BOOK_BONUS)
                .add(CSCAttributes.PHYSICAL_LIFE_STEAL)
                .add(CSCAttributes.MAGICAL_LIFE_STEAL);
    }

    @Inject(method = "blockUsingItem", at = @At("TAIL"))
    private void triggerAbilityOnBlockUse(ServerLevel level, LivingEntity attacker, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        ItemStack blockedStack = player.getItemBlockingWith();

        if (blockedStack == null) return;
        if (blockedStack.isEmpty()) return;

        AbilityComponent ability = blockedStack.get(CSCComponents.ABILITY);
        if (ability == null) return;

        if (!ability.activationTypes().contains(ActivationType.ON_BLOCK)) return;
        ability.onBlockUse(player, blockedStack);
    }
}