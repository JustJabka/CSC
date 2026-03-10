package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.ActiveAbility;
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

public class DarkGauntletAbility extends ActiveAbility {
    private final double attributeDamage;
    private final double tickingDamage;

    private AttributeInstance attribute;
    private static final Identifier ABILITY_DAMAGE_ID = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "dark_gauntlet_ability");

    public DarkGauntletAbility(int duration, double attributeDamage, double tickingDamage) {
        super(true, duration);
        this.attributeDamage = attributeDamage;
        this.tickingDamage = tickingDamage;
    }

    @Override
    public void onStart() {
        attribute = ctx.player.getAttribute(Attributes.ATTACK_DAMAGE);

        // Apply Modifier
        attribute.addTransientModifier(
                new AttributeModifier(
                        ABILITY_DAMAGE_ID,
                        attributeDamage,
                        AttributeModifier.Operation.ADD_VALUE
                ));

        // Apply Enchantment Glint
        ctx.getStack().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        // Play sound
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        // TODO: incoming damage multiplier

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
        return !ctx.getStack().is(CSCItems.DARK_GAUNTLET);
    }

    @Override
    public void onEnd() {
        // Remove Modifier
        attribute.removeModifier(ABILITY_DAMAGE_ID);

        // Remove Enchantment Glint
        // TODO: fix component desync
        ctx.getStack().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        // Play Sound
        ctx.player.level().playSound(null, ctx.player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}