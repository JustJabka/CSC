package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SpinningSwordsAbility extends BaseActiveAbility {
    private final double radius;
    private final double damagePercent;

    public SpinningSwordsAbility(Identifier key, int duration, double radius, double damagePercent) {
        super(key, duration);
        this.radius = radius;
        this.damagePercent = damagePercent;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onTick() {
        // TODO: add visual for ability
        Player player = ctx.player;
        Level level = ctx.level;

        if (player.tickCount % 20 != 0) return;
        damageEntities(player, level);
    }

    private void damageEntities(Player player, Level level) {
        AABB damageRadius = player.getBoundingBox().inflate(radius);
        List<Entity> entities = level.getEntities(player, damageRadius);

        Holder<DamageType> damageType = player.damageSources().magic().typeHolder();
        DamageSource damageSource = new DamageSource(damageType, player);

        entities.forEach(entity -> {
            if (!(entity instanceof LivingEntity victim)) return;

            double maxHealth = victim.getMaxHealth();
            float damage = (float) (maxHealth * damagePercent);

            victim.hurtServer((ServerLevel) level, damageSource, damage);
        });
    }

    @Override
    public void onEnd() {

    }
}
