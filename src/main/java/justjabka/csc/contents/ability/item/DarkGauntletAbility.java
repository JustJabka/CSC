package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseTogglableActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.Map;
import java.util.function.Consumer;

public class DarkGauntletAbility extends BaseTogglableActiveAbility {
    private static final double TICKING_DAMAGE = 0.01;

    private static final double DAMAGE_MODIFIER = 7;
    private static final double VULNERABILITY_MODIFIER = 0.02;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            Attributes.ATTACK_DAMAGE, new AttributeModifier(getId(),
                    DAMAGE_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                    getId(),
                    VULNERABILITY_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            )
    );

    public DarkGauntletAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
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
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifiers(player, ACTIVE_MODIFIERS);

        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_ACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;

        if (player.tickCount % 20 != 0) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Magic Damage = 1% of MaxHP / per sec.
        DamageSource damageSource = serverPlayer.damageSources().magic();
        float damageAmount = (float) (player.getMaxHealth() * TICKING_DAMAGE);

        serverPlayer.hurtServer(serverPlayer.level(), damageSource, damageAmount);
    }

    @Override
    public boolean shouldEnd() {
        return !ctx.getItem().is(CSCItems.DARK_GAUNTLET);
    }

    @Override
    public void onEnd() {
        Player player = ctx.player;

        AttributeHandler.removeModifiers(player, ACTIVE_MODIFIERS);

        // Remove Enchantment Glint
        // TODO: fix component desync
        ctx.getItem().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_DARK_GAUNTLET_DEACTIVATE, SoundSource.PLAYERS, 1f, 1f);
    }
}