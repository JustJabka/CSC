package justjabka.csc.types;

import net.minecraft.ChatFormatting;

public enum AbilityCategory {
    BENEFICIAL(ChatFormatting.GREEN),
    HARMFUL(ChatFormatting.RED),
    NEUTRAL(ChatFormatting.GRAY);

    private final ChatFormatting tooltipFormatting;

    AbilityCategory(final ChatFormatting tooltipFormatting) {
        this.tooltipFormatting = tooltipFormatting;
    }

    public ChatFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
