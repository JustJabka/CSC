package justjabka.csc.rendering;

import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CSCAbilityRendering {
    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        AbilityHandler handler = player.getAttached(CSCAttachments.ABILITY_HANDLER);
        if (handler == null) return;


        for (ActiveAbility ability : handler.getActiveAbilities()) {
            int remainingSeconds = ability.getRemainingSeconds();

            renderAbilities(graphics, font, player, sw, sh, remainingSeconds);
        }
    }

    public static void renderAbilities(
            GuiGraphics graphics,
            Font font,
            Player player,
            int sw,
            int sh,
            int remainingSeconds
    ) {
        Component text = Component
                .literal(String.valueOf(remainingSeconds))
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                );

        graphics.drawCenteredString(font, text, sw / 2, sh / 2, 0xFFFFFFFF);
    }
}