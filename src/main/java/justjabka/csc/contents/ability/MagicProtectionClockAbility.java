package justjabka.csc.contents.ability;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.stream.Stream;

public class MagicProtectionClockAbility extends BaseActiveAbility {
    private final AttributeModifier magicResistanceModifier;

    private AttributeInstance magicResistanceInstance;

    public MagicProtectionClockAbility(Identifier key, int duration, AttributeModifier magicResistance) {
        super(key, duration);
        this.magicResistanceModifier = magicResistance;
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        magicResistanceInstance = player.getAttribute(CSCAttributes.MAGIC_RESISTANCE);
        magicResistanceInstance.addTransientModifier(magicResistanceModifier);

        player.setGlowingTag(true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_MAGIC_PROTECTION_CLOCK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {
        Player player = ctx.player;

        Stream<Holder<MobEffect>> activeEffects = player.getActiveEffectsMap().keySet().stream();

        List<Holder<MobEffect>> harmfulEffects = activeEffects.filter(effect -> {
            MobEffectCategory effectCategory = effect.value().getCategory();
            return effectCategory == MobEffectCategory.HARMFUL;
        }).toList();

        harmfulEffects.forEach(player::removeEffect);
    }

    @Override
    public void onEnd() {
        ctx.player.setGlowingTag(false);
        magicResistanceInstance.removeModifier(key);
    }
}