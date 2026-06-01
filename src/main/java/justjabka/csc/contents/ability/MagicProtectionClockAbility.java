package justjabka.csc.contents.ability;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public class MagicProtectionClockAbility extends BaseActiveAbility {
    public static final Identifier MAGIC_PROTECTION_CLOCK_ABILITY_KEY = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "magic_protection_clock");
    public static final double MAGIC_RESISTANCE = 1;

    private AttributeInstance magicResistanceInstance;

    public MagicProtectionClockAbility(int duration) {
        super(MAGIC_PROTECTION_CLOCK_ABILITY_KEY, duration);
    }

    @Override
    public void onStart() {
        Player player = ctx.player;

        magicResistanceInstance = player.getAttribute(CSCAttributes.MAGIC_RESISTANCE);
        magicResistanceInstance.addTransientModifier(
                new AttributeModifier(
                        key,
                        MAGIC_RESISTANCE,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );

        player.setGlowingTag(true);
        player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_MAGIC_PROTECTION_CLOCK, SoundSource.PLAYERS, 1f, 1f);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        ctx.player.setGlowingTag(false);
        magicResistanceInstance.removeModifier(key);
    }
}