package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.events.OnPlayerHealthChangeCallback;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.ShardContext;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.Consumer;

public class BerserkCharacter extends BaseCharacter implements OnPlayerHealthChangeCallback, ServerLivingEntityEvents.AllowDeath {
    private static final int LOST_HEALTH_BONUS_PERCENT_STEP = 10;
    private static final double LOST_HEALTH_ATTACK_DAMAGE_BONUS_PERCENT = 0.03;
    private static final double LOST_HEALTH_ATTACK_SPEED_BONUS_PERCENT = 0.02;

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "berserk");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public Identifier getShardAbility() {
        return CSCAbilities.BERSERK_SHARD.getId();
    }

    @Override
    public int getShardCooldown() {
        return TimeHandler.minutesToTicks(2);
    }

    @Override
    public int getShardDuration() {
        return TimeHandler.secondsToTicks(10);
    }

    @Override
    public void getShardDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("shard.csc.berserk.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("shard.csc.berserk.description.2", CSCItems.BLOODY_LARYNX.getDefaultInstance().getItemName()).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("shard.csc.berserk.description.3").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 18.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of();
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.BLOODY_LARYNX, 0
        );
    }

    @Override
    public void onShardTrigger(ShardContext ctx) {
        Player player = ctx.player;
        ItemStack shardStack = ctx.shardStack;

        player.setHealth(0.1f);

        triggerInvulnerability(player, shardStack);
        triggerFirstAbility(player);
    }

    @Override
    public void onChange(Player player, float oldHealth, float newHealth) {
        float maxHealth = player.getMaxHealth();
        float lostHealthPercent = (maxHealth - newHealth) / maxHealth;

        int step = (int) (lostHealthPercent * LOST_HEALTH_BONUS_PERCENT_STEP);

        double attackDamageBonus = step * LOST_HEALTH_ATTACK_DAMAGE_BONUS_PERCENT;
        double attackSpeedBonus = step * LOST_HEALTH_ATTACK_SPEED_BONUS_PERCENT;

        updatePassiveAbilityBonus(player, Attributes.ATTACK_DAMAGE, attackDamageBonus);
        updatePassiveAbilityBonus(player, Attributes.ATTACK_SPEED, attackSpeedBonus);
    }

    @Override
    public boolean allowDeath(@NonNull LivingEntity entity, @NonNull DamageSource source, float amount) {
        if (!(entity instanceof Player player)) return true;

        // Check if player have shard
        ItemStack stack = TrinketHandler.findFirstTrinket(player, CSCItems.SHARD, "legs/belt");
        if (stack.isEmpty()) return true;

        // Check if shard is on cooldown
        ItemCooldowns cooldowns = player.getCooldowns();
        boolean isOnCooldown = cooldowns.isOnCooldown(stack);

        // Apply cooldown
        if (isOnCooldown) return true;
        cooldowns.addCooldown(stack, getShardCooldown());

        // Trigger shard
        ShardContext ctx = new ShardContext(player, stack, this);
        onShardTrigger(ctx);

        return false;
    }

    private void updatePassiveAbilityBonus(Player player, Holder<Attribute> attribute, double value) {
        AttributeModifier modifier = new AttributeModifier(
                getKey(),
                value,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        AttributeHandler.removeModifier(player, attribute, modifier);

        if (value <= 0) return;

        AttributeHandler.addTransientModifier(player, attribute, modifier);
    }

    private void triggerFirstAbility(Player player) {
        ItemStack stack = TrinketHandler.getFirstTrinket(player, ABILITY_SLOT_ID);
        if (stack.isEmpty()) return;

        AbilityComponent ability = stack.get(CSCComponents.ABILITY);
        if (ability == null) return;

        AbilityContext ctx = new AbilityContext(player, stack);
        ability.forceActivate(ctx);
    }

    private void triggerInvulnerability(Player player, ItemStack shardStack) {
        AbilityContext ctx = new AbilityContext(player, shardStack);
        BaseActiveAbility ability = CSCAbilities.BERSERK_SHARD.createInstance(getShardDuration(), ctx);

        AbilityHandler.addAbility(player, ability);
    }
}
