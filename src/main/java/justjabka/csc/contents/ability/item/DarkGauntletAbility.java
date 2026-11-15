package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.TogglableAbility;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;
import java.util.function.Consumer;

public class DarkGauntletAbility extends TogglableAbility {
    private static final double TICKING_DAMAGE = 0.01;

    private static final double DAMAGE_MODIFIER = 7;
    private static final double VULNERABILITY_MODIFIER = 0.02;

    public DarkGauntletAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
        addAttributeModifier(CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, VULNERABILITY_MODIFIER, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component
                .translatable("item.csc.dark_gauntlet.description.1", DescriptionHandler.PHYSICAL_DAMAGE, DAMAGE_MODIFIER)
                .withStyle(ChatFormatting.GRAY)
        );
        textConsumer.accept(Component
                .translatable("item.csc.dark_gauntlet.description.2",
                        DescriptionHandler.wrapDecimalAsPercent(VULNERABILITY_MODIFIER),
                        DescriptionHandler.MAGICAL_DAMAGE,
                        DescriptionHandler.wrapDecimalAsPercent(TICKING_DAMAGE),
                        DescriptionHandler.MAX_HEALTH
                )
                .withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public boolean onTick(AbilityContext ctx) {
        Player player = ctx.player;

        if (player.tickCount % 20 != 0) return true;
        if (!(player instanceof ServerPlayer serverPlayer)) return true;

        // Magic Damage = 1% of MaxHP / per sec.
        DamageSource damageSource = serverPlayer.damageSources().magic();
        float damageAmount = (float) (player.getMaxHealth() * TICKING_DAMAGE);

        serverPlayer.hurtServer(serverPlayer.level(), damageSource, damageAmount);

        return true;
    }

    @Override
    public boolean shouldEnd(AbilityContext ctx) {
        Optional<EquipmentSlot> slot = ctx.getSlot();

        if (slot.isPresent()) {
            Item item = ctx.getItem().getItem();
            return !ctx.player.getItemBySlot(slot.get()).is(item);
        }

        return true;
    }

    @Override
    public void onEnd(AbilityContext ctx) {
        Player player = ctx.player;

        // Remove Enchantment Glint
        Item item = ctx.getItem().getItem();

        for (ItemStack stack : player.getInventory()) {
            if (!stack.is(item)) continue;
            Boolean glint = stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);

            if (glint == null) continue;
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        }

        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}