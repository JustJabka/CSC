package justjabka.csc.mixin;

import justjabka.csc.data.CSCDamageTypeTagProvider;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"
	), method = "hurtServer", cancellable = true)
	public void hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		AttributeInstance instance = self.getAttribute(CSCAttributes.DODGE_CHANCE);

		if (instance != null) {
			double dodgeChance = instance.getValue();
			boolean bypassesDodge = damageSource.is(CSCDamageTypeTagProvider.BYPASSES_DODGE);

			if (self.getRandom().nextDouble() < dodgeChance & !bypassesDodge) {
				self.level().playSound(null, self.blockPosition(), CSCSounds.PLAYER_DODGE, SoundSource.PLAYERS, 1f, 1f);
				cir.setReturnValue(false);
			}
		}
	}
}