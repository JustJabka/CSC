package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.AbilityType;
import justjabka.csc.types.ActivationType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public record AbilityComponent(Identifier id, int duration, Set<ActivationType> activationTypes) implements TooltipProvider {
    public static final Codec<Set<ActivationType>> SET_CODEC = ActivationType.CODEC.listOf().xmap(
        Set::copyOf,
        List::copyOf
    );

    public AbilityComponent(Identifier id, int duration) {
        this(id, duration, Set.of(ActivationType.GENERIC));
    }

    public static final Codec<AbilityComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Identifier.CODEC.fieldOf("id").forGetter(AbilityComponent::id),
                Codec.INT.fieldOf("duration").forGetter(AbilityComponent::duration),
                SET_CODEC.optionalFieldOf("activation_types", Set.of(ActivationType.GENERIC)).forGetter(AbilityComponent::activationTypes)
        ).apply(builder, AbilityComponent::new);
    });

    @Override
    @Environment(EnvType.CLIENT)
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        // Default Description
        UseCooldown useCooldown = components.get(DataComponents.USE_COOLDOWN);

        if (useCooldown != null) {
            textConsumer.accept(Component.translatable("other.csc.cooldown", TimeHandler.autoConvertTicks(useCooldown.ticks())).withStyle(ChatFormatting.YELLOW));
        }

        if (this.duration > 0) {
            textConsumer.accept(Component.translatable("other.csc.duration", TimeHandler.autoConvertTicks(this.duration)).withStyle(ChatFormatting.GREEN));
        }

        // Ability Description
        Minecraft mc = Minecraft.getInstance();

        LocalPlayer player = mc.player;
        if (player == null) return;

        BaseActiveAbility abilityInstance = createInstance(new AbilityContext(player, ItemStack.EMPTY));
        if (abilityInstance == null) return;

        abilityInstance.getDescription(context, textConsumer, type, components);
    }

    public InteractionResult onUse(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot(), item, null);
        return tryActivate(ctx);
    }

    public InteractionResult onInteractionUse(ItemStack item, Player player, LivingEntity target, InteractionHand hand) {
        AbilityContext ctx = new AbilityContext(player, hand.asEquipmentSlot(), item, target);
        return tryActivate(ctx);
    }

    public InteractionResult onTrinketUse(Player player, ItemStack item) {
        AbilityContext ctx = new AbilityContext(player, item);
        return tryActivate(ctx);
    }

    public InteractionResult onBlockUse(Player player, ItemStack item) {
        AbilityContext ctx = new AbilityContext(player, item);
        return tryActivate(ctx);
    }

    public InteractionResult tryActivate(AbilityContext ctx) {
        Player player = ctx.player;
        Level level = ctx.level;
        ItemStack item = ctx.getItem();

        BaseActiveAbility abilityInstance = createInstance(ctx);

        if (level.isClientSide()) return InteractionResult.PASS;
        if (abilityInstance == null) return InteractionResult.PASS;
        if (isOnCooldown(player, item)) {
            level.playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
            return InteractionResult.FAIL;
        }
        if (!abilityInstance.canActivate(ctx)) return InteractionResult.FAIL;

        applyCooldown(player, item);
        activate(ctx);

        if (player.isUsingItem()) {
            player.stopUsingItem();
        }

        return InteractionResult.SUCCESS;
    }

    public void activate(AbilityContext ctx) {
        BaseActiveAbility abilityInstance = createInstance(ctx);

        if (abilityInstance == null) return;

        AbilityHandler handler = ctx.player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        handler.addAbility(abilityInstance);
    }

    private BaseActiveAbility createInstance(AbilityContext ctx) {
        AbilityType<?> abilityType = CSCAbilities.getByKey(this.id);

        if (abilityType == null) return null;

        return abilityType.createInstance(this.duration, ctx);
    }

    public void forceActivate(AbilityContext ctx) {
        applyCooldown(ctx.player, ctx.getItem());
        activate(ctx);
    }

    public boolean isOnCooldown(Player player, ItemStack item) {
        ItemCooldowns cooldown = player.getCooldowns();
        return cooldown.isOnCooldown(item);
    }

    public void applyCooldown(Player player, ItemStack item) {
        UseCooldown useCooldown = item.get(DataComponents.USE_COOLDOWN);

        if (useCooldown == null) return;

        useCooldown.apply(item, player);
    }

    public void removeCooldown(Player player, ItemStack item) {
        ItemCooldowns cooldown = player.getCooldowns();
        Identifier cooldownGroup = cooldown.getCooldownGroup(item);

        cooldown.removeCooldown(cooldownGroup);
    }
}