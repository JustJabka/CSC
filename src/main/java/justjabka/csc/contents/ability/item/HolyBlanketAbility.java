package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.handlers.DescriptionHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class HolyBlanketAbility extends Ability {
    private AttributeModifier activeModifier;

    private static final int PROTECTION_DURATION = TimeHandler.secondsToTicks(20);
    private static final float STRONG_PROTECTION_HEALTH_MULTIPLIER = 0.6f;
    private static final float WEAK_PROTECTION_HEALTH_MULTIPLIER = 0.3f;

    public HolyBlanketAbility(AbilityCategory category) {
        super(category);
    }

    @Override
    public void displayDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.2",
                DescriptionHandler.wrapDecimalAsPercent(WEAK_PROTECTION_HEALTH_MULTIPLIER),
                DescriptionHandler.MAX_HEALTH
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.3").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.4",
                DescriptionHandler.wrapDecimalAsPercent(STRONG_PROTECTION_HEALTH_MULTIPLIER),
                DescriptionHandler.MAX_HEALTH
        ).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.holy_blanket.description.5").withStyle(ChatFormatting.GRAY));
    }

    public enum State {
        PARRYING,
        ACTIVATED
    }
    private State state = State.PARRYING;

    @Override
    public void onStart(AbilityContext ctx) {
        this.state = State.PARRYING;
    }

    @Override
    public void onEnd(AbilityContext ctx) {
        if (state != State.ACTIVATED) return;

        AttributeHandler.removeModifier(ctx.player, Attributes.MAX_ABSORPTION, activeModifier);
    }

    public void activateWeakProtection(AbilityContext ctx) {
        Player player = ctx.player;

        player.setHealth(player.getMaxHealth() * WEAK_PROTECTION_HEALTH_MULTIPLIER);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_HOLY_BLANKET_BREAK, SoundSource.PLAYERS, 1f, 1f);
    }

    // TODO: fix
    public void activateStrongProtection(AbilityContext ctx, float damageAmount) {
        this.state = State.ACTIVATED;
//        this.duration = PROTECTION_DURATION;

        Player player = ctx.player;
        player.setHealth(player.getMaxHealth() * STRONG_PROTECTION_HEALTH_MULTIPLIER);

//        activeModifier = new AttributeModifier(
//                getId(),
//                damageAmount,
//                AttributeModifier.Operation.ADD_VALUE
//        );
//
//        AttributeHandler.addTransientModifier(player, Attributes.MAX_ABSORPTION, activeModifier);
//        player.setAbsorptionAmount(player.getAbsorptionAmount() + damageAmount);

        ctx.level.playSound(null, player.blockPosition(), CSCSounds.ITEM_HOLY_BLANKET_BREAK, SoundSource.PLAYERS, 0.5f, 1f);
    }

    public State getState() {
        return this.state;
    }
}
