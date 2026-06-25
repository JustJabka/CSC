package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MagicProtectionClockAbility extends BaseActiveAbility {
    private final AttributeModifier MAGIC_RESISTANCE_MODIFIER = new AttributeModifier(
            getId(),
            1,
            AttributeModifier.Operation.ADD_VALUE
    );


    public MagicProtectionClockAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.magic_protection_clock.description",
                DescriptionHandler.MAGICAL_DAMAGE,
                DescriptionHandler.wrapDecimalAsPercent(MAGIC_RESISTANCE_MODIFIER.amount())
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        AttributeHandler.addTransientModifier(
                player,
                CSCAttributes.MAGIC_RESISTANCE,
                MAGIC_RESISTANCE_MODIFIER
        );

        player.setGlowingTag(true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_MAGIC_PROTECTION_CLOCK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;

        Set<Holder<MobEffect>> activeEffects = player.getActiveEffectsMap().keySet();

        for (Holder<MobEffect> effect : new HashSet<>(activeEffects)) {
            MobEffectCategory effectCategory = effect.value().getCategory();

            if (effectCategory != MobEffectCategory.HARMFUL) continue;

            player.removeEffect(effect);
        }
    }

    @Override
    public void onEnd() {
        Player player = ctx.player;

        player.setGlowingTag(false);
        AttributeHandler.removeModifier(
                player,
                CSCAttributes.MAGIC_RESISTANCE,
                MAGIC_RESISTANCE_MODIFIER
        );
    }
}