package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpinningSwordsAbility extends BaseActiveAbility {
    private boolean hasShard = false;
    private final List<Display.ItemDisplay> swords = new ArrayList<>();

    private static final ItemStack SWORD_ITEM = new ItemStack(Items.IRON_SWORD);

    private static final double RADIUS = 1.5;
    private static final double DAMAGE_PERCENT = 0.08;
    private static final double DAMAGE_PERCENT_SHARD_BONUS = 0.02;

    public SpinningSwordsAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void onStart() {
        Player player = ctx.player;
        Level level = ctx.level;

        hasShard = TrinketHandler.hasTrinket(player, CSCItems.SHARD, "legs/belt");

        int swordsToSpawn = hasShard ? 3 : 2;

        for (int i = 0; i < swordsToSpawn; i++) {
            spawnSword(level, player);
        }

        level.playSound(null, player.blockPosition(), CSCSounds.ABILITY_SPINNING_SWORDS, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;
        Level level = ctx.level;

        updateSwordsDisplay(player, level);

        if (player.tickCount % 20 != 0) return;
        damageEntities(player, level);
    }

    private void spawnSword(Level level, Player player) {
        Display.ItemDisplay sword = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, level);
        sword.setItemStack(SWORD_ITEM);
        sword.setPosRotInterpolationDuration(1);

        sword.setPos(player.position());

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(sword);
        }

        swords.add(sword);
    }

    private void updateSwordsDisplay(Player player, Level level) {
        int count = swords.size();
        if (count == 0) return;

        float baseAngle = (player.tickCount * 10f) % 360f;
        float angleStep = 360f / count;

        double orbitRadius = RADIUS;
        double targetY = player.getY() + (player.getBbHeight() / 2);

        for (int i = 0; i < count; i++) {
            Display.ItemDisplay sword = swords.get(i);

            float currentAngle = (baseAngle + (i * angleStep)) % 360f;
            double radians = Math.toRadians(currentAngle);

            double offsetX = Math.cos(radians) * orbitRadius;
            double offsetZ = Math.sin(radians) * orbitRadius;

            // -currentAngle for rotation around its own axis

            sword.teleportTo(
                    (ServerLevel) level,
                    player.getX() + offsetX,
                    targetY,
                    player.getZ() + offsetZ,
                    Set.of(),
                    currentAngle + 45f + 180f, // Point the blades of the swords in the opposite direction from the user
                    90f,
                    false
            );
        }
    }

    private void damageEntities(Player player, Level level) {
        AABB damageRadius = player.getBoundingBox().inflate(RADIUS);
        List<Entity> entities = level.getEntities(player, damageRadius);

        Holder<DamageType> damageType = player.damageSources().magic().typeHolder();
        DamageSource damageSource = new DamageSource(damageType, player);
        double currentDamagePercent = hasShard ? DAMAGE_PERCENT + DAMAGE_PERCENT_SHARD_BONUS : DAMAGE_PERCENT;

        entities.forEach(entity -> {
            if (!(entity instanceof LivingEntity victim)) return;

            double maxHealth = victim.getMaxHealth();
            float damage = (float) (maxHealth * currentDamagePercent);

            victim.hurtServer((ServerLevel) level, damageSource, damage);
        });
    }

    @Override
    public void onEnd() {
        swords.forEach(Entity::discard);
    }
}