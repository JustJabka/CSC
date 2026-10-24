package justjabka.csc.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DescriptionHandler {

    private DescriptionHandler() {}

    public static final Component PHYSICAL_DAMAGE = Component.translatable("damageType.csc.physical").withStyle(ChatFormatting.RED);
    public static final Component MAGICAL_DAMAGE = Component.translatable("damageType.csc.magical").withStyle(ChatFormatting.LIGHT_PURPLE);
    public static final Component MAX_HEALTH = Component.translatable("attribute.name.max_health").withStyle(ChatFormatting.GREEN);
    public static final Component HEALTH_REGENERATION = Component.translatable("attribute.name.csc.health_regeneration").withStyle(ChatFormatting.GREEN);

    public static String wrapDecimalAsPercent(double value) {
        int percent = Math.toIntExact(Math.round(value * 100));
        return percent + "%";
    }
}
