package justjabka.csc.handlers;

import justjabka.csc.CSC;
import justjabka.csc.types.WaveScaleType;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class WaveHandler {

    private WaveHandler() {}

    public static final Identifier WAVE_SCALE_KEY = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "wave_scale");

    public static void scaleEntityAttributes(LivingEntity entity, int wave) {
        if (isStaticWave(wave)) return;

        double totalMultiplier = 1;

        for (int i = 1; i <= wave; i++) {
            if (isStaticWave(wave)) return;
            totalMultiplier *= (1 + WaveScaleType.getFromWave(i).getAttributeMultiplier());
        }

        totalMultiplier--;

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
}
