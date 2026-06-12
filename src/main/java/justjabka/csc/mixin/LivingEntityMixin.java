package justjabka.csc.mixin;

import justjabka.csc.contents.ability.item.DarkCapeAbility;
import justjabka.csc.data.CSCDamageTypeTagProvider;
import justjabka.csc.events.OnPlayerHealthChangeCallback;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(method = "setHealth", at = @At("HEAD"))
	private void onHealthChange(float health, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;

		if (!(entity instanceof Player player)) return;
		if (player.level().isClientSide()) return;

		float oldHealth = player.getHealth();

		OnPlayerHealthChangeCallback.EVENT.invoker().onChange(player, oldHealth, health);
	}

	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "damage")
	private float modifyIncomingDamage(float damage, ServerLevel level, DamageSource source) {
		LivingEntity entity = (LivingEntity) (Object) this;

		damage = handleDarkCapeAbilityAttack(damage, source);
		damage = handleMagicDamageAttribute(damage, source);
		damage = handleIncomingDamageMultiplierAttribute(entity, damage);

		damage = handleMagicResistanceAttribute(entity, damage, source);

		return damage;
	}

	// TODO: split this attribute to: physical, magical and pure damage
	@Unique
	private float handleIncomingDamageMultiplierAttribute(LivingEntity entity, float damage) {
		AttributeInstance incomingDamageInstance = entity.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);
		if (incomingDamageInstance == null) return damage;

		float multiplier = (float) incomingDamageInstance.getValue();
		if (multiplier == 1.0f) return damage;

		return damage * multiplier;
	}

	@Unique
	private float handleMagicDamageAttribute(float damage, DamageSource source) {
		if (!(source.getEntity() instanceof LivingEntity attacker)) return damage;

		AttributeInstance magicDamageInstance = attacker.getAttribute(CSCAttributes.MAGIC_DAMAGE);
		if (magicDamageInstance == null) return damage;

		if (!source.is(CSCDamageTypeTagProvider.IS_MAGIC)) return damage;

		float multiplier = (float) magicDamageInstance.getValue();
		if (multiplier == 1.0f) return damage;

		return damage * multiplier;
	}

	@Unique
	private float handleMagicResistanceAttribute(LivingEntity entity, float damage, DamageSource source) {
		AttributeInstance magicResistanceInstance = entity.getAttribute(CSCAttributes.MAGIC_RESISTANCE);
		if (magicResistanceInstance == null) return damage;

		if (!source.is(CSCDamageTypeTagProvider.IS_MAGIC)) return damage;

		float resistance = (float) magicResistanceInstance.getValue();
		if (resistance == 0.0f) return damage;

		float resistancePercent = Math.max(1 - resistance, 0);
		return resistancePercent * damage;
	}

	@Unique
	private float handleDarkCapeAbilityAttack(float damage, DamageSource source) {
		if (!(source.getEntity() instanceof Player attacker)) return damage;
		if (!source.is(DamageTypes.PLAYER_ATTACK)) return damage;

		AbilityHandler handler = attacker.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
		if (!handler.hasAbility(DarkCapeAbility.class)) return damage;

		DarkCapeAbility darkCapeAbility = handler.getAbility(DarkCapeAbility.class);
		if (darkCapeAbility == null) return damage;

		// End ability
		darkCapeAbility.end();
		handler.getActiveAbilities().remove(darkCapeAbility);

        return (float) (damage * darkCapeAbility.damageMultiplier);
	}

	@Inject(
			method = "hurtServer", cancellable = true,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V")
	)
	public void hurtServer(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;

		handleDodgeLogic(source, cir, self);
		handleDamageReflectionLogic(level, source, damage, self);
	}

	@Unique
    private static void handleDamageReflectionLogic(ServerLevel level, DamageSource damageSource, float damage, LivingEntity self) {
		AttributeInstance damageReflectionPercentInstance = self.getAttribute(CSCAttributes.DAMAGE_REFLECTION_PERCENT);
		if (damageReflectionPercentInstance == null) return;
		float damageReflectionPercent = (float) damageReflectionPercentInstance.getValue();

		Entity attacker = damageSource.getEntity();
		if (!(attacker instanceof LivingEntity livingAttacker)) return;

		boolean isSelfDamage = attacker == self;
		boolean isValidDamage = damageReflectionPercent > 0;

		if (isSelfDamage || !isValidDamage) return;

		DamageSources damageSources = level.damageSources();
		DamageSource reflectedDamage = damageSources.magic();

		livingAttacker.hurtServer(level, reflectedDamage, damage * damageReflectionPercent);
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