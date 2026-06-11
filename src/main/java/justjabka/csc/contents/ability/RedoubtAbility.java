package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class RedoubtAbility extends BaseActiveAbility {
    private boolean hasShard = false;
    private AttributeModifier absorptionAmountShardBonusModifier;

    private final float absorptionAmountShardBonus;
    private final Map<Holder<Attribute>, AttributeModifier> activeModifiers;

    public RedoubtAbility(Identifier key, int duration, double incomingDamageMultiplierModifier, double knockbackResistanceModifier, float absorptionAmountShardBonus) {
        super(key, duration);
        this.activeModifiers = Map.of(
                CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                        key,
                        incomingDamageMultiplierModifier,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                        key,
                        knockbackResistanceModifier,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
        this.absorptionAmountShardBonus = absorptionAmountShardBonus;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        hasShard = TrinketHandler.hasTrinket(player, CSCItems.SHARD, "legs/belt");

        if (hasShard) giveShardBonus(player);
        AttributeHandler.addTransientModifiers(player, activeModifiers);

        ctx.level.playSound(null, player.blockPosition(), CSCSounds.ABILITY_REDOUBT, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;

        if (!(ctx.level instanceof ServerLevel serverLevel)) return;

        serverLevel.sendParticles (
                ParticleTypes.POOF,
                player.getX(),
                player.getBoundingBox().minY + 1,
                player.getZ(),
                1,
                0.25, 0.5, 0.25,
                0
        );
    }

    @Override
    public void onEnd() {
        Player player = ctx.player;

        if (hasShard) clearShardBonus(player);
        AttributeHandler.removeModifiers(player, activeModifiers);
    }

    private void giveShardBonus(Player player) {
        float currentAmount = player.getAbsorptionAmount();
        float bonusAmount = player.getMaxHealth() * absorptionAmountShardBonus;

        absorptionAmountShardBonusModifier = new AttributeModifier(
                key,
                bonusAmount,
                AttributeModifier.Operation.ADD_VALUE
        );

        AttributeHandler.addTransientModifier(player, Attributes.MAX_ABSORPTION, absorptionAmountShardBonusModifier);
        player.setAbsorptionAmount(currentAmount + bonusAmount);
    }

    private void clearShardBonus(Player player) {
        AttributeHandler.removeModifier(player, Attributes.MAX_ABSORPTION, absorptionAmountShardBonusModifier);
    }
}
