package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Consumer;

public class BloodyLarynx extends BaseActiveTrinketItem {
    private static final double DAMAGE_MULTIPLIER = 2;
    private static final double MIN_RADIUS = 3;
    private static final double MAX_RADIUS = 15;

    public BloodyLarynx(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "bloody_larynx");
    }

    @Override
    protected int getCooldown() {
        return 60;
    }

    @Override
    protected int getDuration() {
        return 0;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return null;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        double attackDamage = 1;
        int lostHealth = 0;

        if (player != null) {
            attackDamage = getAttackDamage(player);
            lostHealth = (int) getLostHealth(player);
        }

        showFormula(textConsumer, mc, attackDamage, lostHealth);
        textConsumer.accept(Component.translatable("item.csc.bloody_larynx.description.2", MIN_RADIUS, MAX_RADIUS).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.bloody_larynx.description.3").withStyle(ChatFormatting.GRAY));
        if (!mc.hasShiftDown()) textConsumer.accept(Component.translatable("other.csc.reveal_details", Component.translatable("key.sneak")).withStyle(ChatFormatting.DARK_GRAY));
    }

    private static void showFormula(Consumer<Component> textConsumer, Minecraft mc, double attackDamage, int lostHealth) {
        if (mc.hasShiftDown()) {
            textConsumer.accept(Component.translatable("item.csc.bloody_larynx.description.1",
                    Component.translatable("attribute.name.attack_damage"),
                    Component.translatable("health.csc.lost"),
                    DAMAGE_MULTIPLIER,
                    PHYSICAL_DAMAGE
            ).withStyle(ChatFormatting.GRAY));
        } else {
            textConsumer.accept(Component.translatable("item.csc.bloody_larynx.description.1",
                    attackDamage,
                    lostHealth,
                    DAMAGE_MULTIPLIER,
                    PHYSICAL_DAMAGE
            ).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    protected void onUse(AbilityContext ctx) {
        Player player = ctx.player;
        Level level = ctx.level;

        double lostHealth = getLostHealth(player);
        double attackDamage = getAttackDamage(player);

        double damage = attackDamage + (lostHealth * DAMAGE_MULTIPLIER);
        double radius = getRadius(player);

        damageEntities(player, level, damage, radius);
        onUseEffects(player, level, lostHealth, radius);
    }

    private static void damageEntities(Player player, Level level, double damage, double radius) {
        AABB damageRadius = player.getBoundingBox().inflate(radius);
        List<Entity> entities = level.getEntities(player, damageRadius);

        Holder<DamageType> damageType = player.damageSources().playerAttack(player).typeHolder();
        DamageSource damageSource = new DamageSource(damageType, player);

        entities.forEach(entity -> {
            if (!(entity instanceof LivingEntity victim)) return;

            victim.hurtServer((ServerLevel) level, damageSource, (float) damage);
        });
    }

    private static void onUseEffects(Player player, Level level, double lostHealth, double radius) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        float minPitch = 0.7f;
        float maxPitch = 1.2f;
        float pitch = maxPitch - (minPitch * (float) (lostHealth / player.getMaxHealth()));

        for (int j = 0; j < radius; j++) {
            int particleCount = j * 8;

            for (int i = 0; i < particleCount; i++) { // TODO: how the fuck do I even schedule ts?!
                double angle = (i * 2 * Math.PI) / particleCount;

                double xOffset = Math.cos(angle) * j;
                double zOffset = Math.sin(angle) * j;

                serverLevel.sendParticles(
                        new DustParticleOptions(16711680, 2f),
                        player.getX() + xOffset,
                        player.getY() + 1.0,
                        player.getZ() + zOffset,
                        1,
                        0.0, 0.0, 0.0,
                        0.0
                );
            }
        }

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2f, pitch);
        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 2f, pitch);
    }

    private static double getLostHealth(Player player) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        return maxHealth - currentHealth;
    }

    private static double getAttackDamage(Player player) {
        AttributeInstance attackDamageInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
        return attackDamageInstance == null ? 1.0 : attackDamageInstance.getValue();
    }

    private static double getRadius(Player player) {
        double maxHealth = player.getMaxHealth();
        if (maxHealth <= 0) return MIN_RADIUS;

        double lostHealth = getLostHealth(player);
        double lostHealthPercent = lostHealth / maxHealth;

        return MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * lostHealthPercent;
    }
}
