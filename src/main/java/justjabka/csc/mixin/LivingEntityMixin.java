package justjabka.csc.mixin;

import justjabka.csc.contents.ability.DarkCapeAbility;
import justjabka.csc.data.CSCDamageTypeTagProvider;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "f")
	private float handleIncomingDamageMultiplierAttribute(float f, ServerLevel serverLevel, DamageSource damageSource) {
		LivingEntity self = (LivingEntity) (Object) this;

		AttributeInstance incomingDamageInstance = self.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);
		if (incomingDamageInstance == null) return f;

		float multiplier = (float) incomingDamageInstance.getValue();
		if (multiplier == 1.0f) return f;

		return f * multiplier;
	}

	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "f")
	private float handleDarkCapeAbilityAttack(float f, ServerLevel serverLevel, DamageSource damageSource) {
		if (!(damageSource.getEntity() instanceof Player attacker)) return f;
		if (!damageSource.is(DamageTypes.PLAYER_ATTACK)) return f;

		AbilityHandler handler = attacker.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
		if (!handler.hasAbility(DarkCapeAbility.class)) return f;

		DarkCapeAbility darkCapeAbility = handler.getAbility(DarkCapeAbility.class);
		if (darkCapeAbility == null) return f;

		// End ability
		darkCapeAbility.end();
		handler.getActiveAbilities().remove(darkCapeAbility);

		return f * darkCapeAbility.DAMAGE_MULTIPLIER;
	}

	@Inject(
			method = "hurtServer", cancellable = true,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V")
	)
	public void hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;

		handleDodgeLogic(damageSource, cir, self);
		handleDamageReflectionLogic(serverLevel, damageSource, f, self);
	}

	@Unique
    private static void handleDamageReflectionLogic(ServerLevel serverLevel, DamageSource damageSource, float damageAmount, LivingEntity self) {
		AttributeInstance damageReflectionPercentInstance = self.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);
		if (damageReflectionPercentInstance == null) return;
		float damageReflectionPercent = (float) damageReflectionPercentInstance.getValue();

		Entity attacker = damageSource.getEntity();
		if (!(attacker instanceof LivingEntity livingAttacker)) return;

		boolean isSelfDamage = attacker == self;
		boolean isValidDamage = damageReflectionPercent > 0;

		if (isSelfDamage || !isValidDamage) return;

		DamageSources damageSources = serverLevel.damageSources();
		DamageSource reflectedDamage = damageSources.magic();

		livingAttacker.hurtServer(serverLevel, reflectedDamage, damageAmount * damageReflectionPercent);
	}

	@Unique
    private static void handleDodgeLogic(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir, LivingEntity self) {
		AttributeInstance dodgeChanceInstance = self.getAttribute(CSCAttributes.DODGE_CHANCE);
		if (dodgeChanceInstance == null) return;

		double dodgeChance = dodgeChanceInstance.getValue();
		boolean bypassesDodge = damageSource.is(CSCDamageTypeTagProvider.BYPASSES_DODGE);

		boolean isDodgeTriggered = self.getRandom().nextDouble() < dodgeChance;
		boolean canDodge = isDodgeTriggered  && !bypassesDodge;

		if (!canDodge) return;

		self.level().playSound(null, self.blockPosition(), CSCSounds.PLAYER_DODGE, SoundSource.PLAYERS, 1f, 1f);
		cir.setReturnValue(false);
	}
}