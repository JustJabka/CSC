package justjabka.csc.contents.ability;

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
import net.minecraft.world.entity.player.Player;

public class DarkGauntletAbility extends BaseTogglableActiveAbility {
    private final double tickingDamage;
    private final AttributeModifier damageModifier;
    private final AttributeModifier incomingDamageModifier;

    private AttributeInstance damageInstance;
    private AttributeInstance incomingDamageInstance;

    public DarkGauntletAbility(Identifier key, int duration, AttributeModifier damageModifier, AttributeModifier incomingDamageModifier, double tickingDamage) {
        super(key, duration);
        this.damageModifier = damageModifier;
        this.incomingDamageModifier = incomingDamageModifier;
        this.tickingDamage = tickingDamage;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        try {
            damageInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
            incomingDamageInstance = player.getAttribute(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER);

            // Apply Modifier
            damageInstance.addTransientModifier(damageModifier);
            incomingDamageInstance.addTransientModifier(incomingDamageModifier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
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
        damageInstance.removeModifier(key);
        incomingDamageInstance.removeModifier(key);

        // Remove Enchantment Glint
        // TODO: fix component desync
        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}