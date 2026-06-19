package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class HolyBlanketAbility extends BaseActiveAbility {
    private AttributeModifier activeModifier;

    private final int protectionDuration;
    private final float strongProtectionHealthMultiplier;

    public enum State {
        PARRYING,
        ACTIVATED
    }
    private State state = State.PARRYING;

    public HolyBlanketAbility(Identifier key, int duration, int protectionDuration, float strongProtectionHealthMultiplier) {
        super(key, duration);
        this.protectionDuration = protectionDuration;
        this.strongProtectionHealthMultiplier = strongProtectionHealthMultiplier;
    }

    @Override
    public void onStart() {
        this.state = State.PARRYING;
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        if (state != State.ACTIVATED) return;

        AttributeHandler.removeModifier(ctx.player, Attributes.MAX_ABSORPTION, activeModifier);
    }

    public void activateStrongProtection(float damageAmount) {
        this.state = State.ACTIVATED;
        this.duration = protectionDuration;

        Player player = ctx.player;
        player.setHealth(player.getMaxHealth() * strongProtectionHealthMultiplier);

        activeModifier = new AttributeModifier(
                key,
                damageAmount,
                AttributeModifier.Operation.ADD_VALUE
        );

        AttributeHandler.addTransientModifier(player, Attributes.MAX_ABSORPTION, activeModifier);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + damageAmount);

        ctx.level.playSound(null, player.blockPosition(), CSCSounds.ITEM_HOLY_BLANKET_BREAK, SoundSource.PLAYERS, 0.5f, 1f);
    }

    public State getState() {
        return this.state;
    }
}
