package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class DarkGauntletAbility extends BaseTogglableActiveAbility {
    private final double tickingDamage;
    private final Map<Holder<Attribute>, AttributeModifier> activeModifiers;

    public DarkGauntletAbility(Identifier key, int duration, double tickingDamage, Map<Holder<Attribute>, AttributeModifier> activeModifiers) {
        super(key, duration);

        this.tickingDamage = tickingDamage;
        this.activeModifiers = activeModifiers;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifiers(player, activeModifiers);

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
        return !ctx.getItem().is(CSCItems.DARK_GAUNTLET);
    }

    @Override
    public void onEnd() {
        Player player = ctx.player;

        AttributeHandler.removeModifiers(player, activeModifiers);

        // Remove Enchantment Glint
        // TODO: fix component desync
        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}