package justjabka.csc.handlers;

import justjabka.csc.CSC;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.commons.lang3.Range;

public class WaveHandler {

    public static final Identifier WAVE_SCALE_KEY = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "wave_scale");

    public static void scaleEntityAttributes(LivingEntity entity, int wave) {
        if (isStaticWave(wave)) return;

        double totalMultiplier = 1;

        for (int i = 1; i <= wave; i++) {
            if (isStaticWave(wave)) return;
            totalMultiplier *= (1 + WaveType.getFromWave(i).attributeMultiplier);
        }

        totalMultiplier -= 1;

        scaleAttribute(entity, Attributes.MAX_HEALTH, totalMultiplier);
        scaleAttribute(entity, Attributes.ATTACK_DAMAGE, totalMultiplier);

        entity.setHealth(entity.getMaxHealth());
    }

    private static void scaleAttribute(LivingEntity entity, Holder<Attribute> attribute, double multiplier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;

        instance.addOrReplacePermanentModifier(new AttributeModifier(
                WAVE_SCALE_KEY,
                multiplier,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        ));
    }

    private static boolean isStaticWave(int wave) {
        if (wave < 2) return false;
        return (wave - 2) % 5 == 0;
    }

    // TODO: add proper scale for later waves
    public enum WaveType {
        EARLY_GAME(Range.of(1, 10), 0.2),
        MID_GAME(Range.of(11, 99), 0.1),
        LATE_GAME(Range.of(100, Integer.MAX_VALUE), 0.01);

        private final Range<Integer> startingWave;
        private final double attributeMultiplier;

        WaveType(Range<Integer> startingWave, double attributeMultiplier) {
            this.startingWave = startingWave;
            this.attributeMultiplier = attributeMultiplier;
        }

        public static WaveType getFromWave(int wave) {
            for (WaveType type : WaveType.values()) {
                if (!type.startingWave.contains(wave)) continue;
                return type;
            }

            return LATE_GAME;
        }
    }
}
