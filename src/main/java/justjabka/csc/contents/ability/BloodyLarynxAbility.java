package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BloodyLarynxAbility extends BaseActiveAbility {
    private static final double DAMAGE_MULTIPLIER = 2;
    private static final double MIN_RADIUS = 3;
    private static final double MAX_RADIUS = 15;
    private static final float MIN_PITCH = 0.7f;
    private static final float MAX_PITCH = 1.2f;

    private double maxCalculatedRadius;
    private double damage;

    private int currentRadius = 0;
    private final Set<UUID> damagedEntities = new HashSet<>();

    public BloodyLarynxAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void onStart() {
        Player player = ctx.player;
        Level level = ctx.level;

        if (level.isClientSide()) return;

        double lostHealth = getLostHealth(player);
        double attackDamage = getAttackDamage(player);

        this.damage = attackDamage + (lostHealth * DAMAGE_MULTIPLIER);
        double lostHealthPercent = player.getMaxHealth() <= 0 ? 0 : lostHealth / player.getMaxHealth();
        this.maxCalculatedRadius = MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * lostHealthPercent;

        float pitch = MAX_PITCH - (MIN_PITCH * (float) lostHealthPercent);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2f, pitch);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 2f, pitch);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;
        if (!(ctx.level instanceof ServerLevel serverLevel)) return;

        if (currentRadius > maxCalculatedRadius) {
            forceEnd();
            return;
        }

        displayWaveParticles(serverLevel, player);
        damageEntities(serverLevel, player);

        currentRadius++;
    }

    private void displayWaveParticles(ServerLevel serverLevel, Player player) {
        int particleCount = currentRadius * 8;
        if (particleCount > 0) {
            for (int i = 0; i < particleCount; i++) {
                double angle = (i * 2 * Math.PI) / particleCount;
                double xOffset = Math.cos(angle) * currentRadius;
                double zOffset = Math.sin(angle) * currentRadius;

                serverLevel.sendParticles(
                        new DustParticleOptions(16711680, 2f),
                        player.getX() + xOffset,
                        player.getY() + 1.0,
                        player.getZ() + zOffset,
                        1, 0.0, 0.0, 0.0, 0.0
                );
            }
        }
    }

    private void damageEntities(ServerLevel serverLevel, Player player) {
        AABB damageZone = player.getBoundingBox().inflate(currentRadius);
        List<Entity> entities = serverLevel.getEntities(player, damageZone);

        Holder<DamageType> damageType = player.damageSources().playerAttack(player).typeHolder();
        DamageSource damageSource = new DamageSource(damageType, player);

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity victim)) return;
            if (damagedEntities.contains(victim.getUUID())) continue;

            if (player.distanceTo(victim) <= currentRadius + 1) {
                victim.hurtServer(serverLevel, damageSource, (float) damage);
                damagedEntities.add(victim.getUUID());
            }
        }
    }

    @Override
    public void onEnd() {}

    private static double getLostHealth(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        return maxHealth - currentHealth;
    }

    private static double getAttackDamage(Player player) {
        AttributeInstance attackDamageInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
        return attackDamageInstance == null ? 1.0 : attackDamageInstance.getValue();
    }
}
