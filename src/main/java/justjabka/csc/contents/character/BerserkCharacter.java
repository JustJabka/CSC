package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.ability.shard.BerserkShardAbility;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.ShardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.Consumer;

public class BerserkCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "berserk");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public int getShardCooldown() {
        return 120;
    }

    @Override
    public int getShardDuration() {
        return 10;
    }

    @Override
    public void getShardDescription(@NonNull ItemStack stack, Item.TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
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

    private void triggerFirstAbility(Player player) {
        ItemStack stack = TrinketHandler.getFirstTrinket(player, ABILITY_SLOT_ID);
        if (stack.isEmpty()) return;

        if (!(stack.getItem() instanceof BaseActiveTrinketItem activeItem)) return;

        // Remove Cooldown
        ItemCooldowns cooldowns = player.getCooldowns();
        Identifier cooldownGroup = cooldowns.getCooldownGroup(stack);
        cooldowns.removeCooldown(cooldownGroup);

        // Trigger Ability
        AbilityContext ctx = new AbilityContext(player, stack);
        activeItem.tryActivate(ctx);
    }

    private void triggerInvulnerability(Player player, ItemStack shardStack) {
        AbilityHandler handler = player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        BaseActiveAbility ability = new BerserkShardAbility(getKey(), getShardDuration() * 20);
        AbilityContext ctx = new AbilityContext(player, shardStack);

        handler.addAbility(ability, ctx);
    }
}
