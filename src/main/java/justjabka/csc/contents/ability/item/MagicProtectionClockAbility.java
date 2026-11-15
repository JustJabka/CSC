package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.Set;
import java.util.function.Consumer;

public class MagicProtectionClockAbility extends Ability {
    private static final double MAGIC_RESISTANCE_AMOUNT = 1;

    public MagicProtectionClockAbility(AbilityCategory category) {
        super(category);
        addAttributeModifier(CSCAttributes.MAGIC_RESISTANCE, MAGIC_RESISTANCE_AMOUNT, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.magic_protection_clock.description",
                DescriptionHandler.MAGICAL_DAMAGE,
                DescriptionHandler.wrapDecimalAsPercent(MAGIC_RESISTANCE_AMOUNT)
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void onStart(AbilityContext ctx) {
        Player player = ctx.player;

        player.setGlowingTag(true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_MAGIC_PROTECTION_CLOCK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public boolean onTick(AbilityContext ctx) {
        Player player = ctx.player;

        Set<Holder<MobEffect>> activeEffects = player.getActiveEffectsMap().keySet();

        for (Holder<MobEffect> effect : activeEffects) {
            MobEffectCategory effectCategory = effect.value().getCategory();

            if (effectCategory != MobEffectCategory.HARMFUL) continue;

            player.removeEffect(effect);
        }

        return true;
    }

    @Override
    public void onEnd(AbilityContext ctx) {
        Player player = ctx.player;

        player.setGlowingTag(false);
    }
}