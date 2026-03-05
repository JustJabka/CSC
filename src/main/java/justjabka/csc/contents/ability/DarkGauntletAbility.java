package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DarkGauntletAbility extends ActiveAbility {
    private final double attributeDamage;
    private final double tickingDamage;
    AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
    Identifier ABILITY_DAMAGE_ID = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "dark_gauntlet_ability");

    public DarkGauntletAbility(Player player, InteractionHand hand, int durationTicks, double attributeDamage, double tickingDamage) {
        super(player, hand, durationTicks);
        this.togglable = true;
        this.attributeDamage = attributeDamage;
        this.tickingDamage = tickingDamage;
    }

    @Override
    public void onStart() {
        ItemStack stack = player.getItemInHand(hand);

        // Apply Modifier
        attribute.addTransientModifier(
                new AttributeModifier(
                        ABILITY_DAMAGE_ID,
                        attributeDamage,
                        AttributeModifier.Operation.ADD_VALUE
                ));

        // Apply Enchantment Glint
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

        // Play sound
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    protected void onTick() {
        // TODO: incoming damage multiplier

        if (player.tickCount % 20 != 0) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Magic Damage = 1% of MaxHP / per sec.
        DamageSource damageSource = serverPlayer.damageSources().magic();
        float damageAmount = (float) (player.getMaxHealth() * tickingDamage);

        serverPlayer.hurtServer(serverPlayer.level(), damageSource, damageAmount);
    }

    @Override
    public boolean shouldEnd() {
        // End if gauntlet is not in hands
        ItemStack stack = player.getItemInHand(hand);

        return !stack.is(CSCItems.DARK_GAUNTLET);
    }

    @Override
    public void onEnd() {
        ItemStack stack = player.getItemInHand(hand);

        // Remove Modifier
        attribute.removeModifier(ABILITY_DAMAGE_ID);

        // Remove Enchantment Glint
        // TODO: fix component desync
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        // Play Sound
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}