package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DarkGauntletAbility extends BaseTogglableActiveAbility {
    public static final Identifier DARK_GAUNTLET_ABILITY_KEY = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "dark_gauntlet");

    private final double attributeDamage;
    private final double attributeIncomingDamage;
    private final double tickingDamage;

    private AttributeInstance attackDamageInstance;
    private AttributeInstance incomingDamageMultiplierInstance;


    public DarkGauntletAbility(int duration, double attributeDamage, double attributeIncomingDamage, double tickingDamage) {
        super(DARK_GAUNTLET_ABILITY_KEY, duration);
        this.attributeDamage = attributeDamage;
        this.attributeIncomingDamage = attributeIncomingDamage;
        this.tickingDamage = tickingDamage;
    }

    @Override
    public void onStart() {
        attackDamageInstance = ctx.player.getAttribute(Attributes.ATTACK_DAMAGE);
        incomingDamageMultiplierInstance = ctx.player.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);

        // Apply Modifier
        attackDamageInstance.addTransientModifier(
                new AttributeModifier(
                        key,
                        attributeDamage,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        incomingDamageMultiplierInstance.addTransientModifier(
                new AttributeModifier(
                        key,
                        attributeIncomingDamage,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );

        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        if (ctx.player.tickCount % 20 != 0) return;
        if (!(ctx.player instanceof ServerPlayer serverPlayer)) return;

        // Magic Damage = 1% of MaxHP / per sec.
        DamageSource damageSource = serverPlayer.damageSources().magic();
        float damageAmount = (float) (ctx.player.getMaxHealth() * tickingDamage);

        serverPlayer.hurtServer(serverPlayer.level(), damageSource, damageAmount);
    }

    @Override
    public boolean shouldEnd() {
        // End if gauntlet is not in hands
        return !ctx.getItem().is(CSCItems.DARK_GAUNTLET);
    }

    @Override
    public void onEnd() {
        // Remove Modifier
        attackDamageInstance.removeModifier(key);
        incomingDamageMultiplierInstance.removeModifier(key);

        // Remove Enchantment Glint
        // TODO: fix component desync
        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}