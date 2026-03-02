package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCHealthRendering {
    // Define Textures
    private static final Identifier HEALTH_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_background");
    private static final Identifier HEALTH_BAR_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_progress");

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        int currentHealth = Math.round(player.getHealth());
        int maxHealth = Math.round(player.getMaxHealth());
        float healthPercent = (currentHealth / (float) maxHealth);

        renderBar(graphics, healthPercent, sw, sh);
        renderText(graphics, font, currentHealth, maxHealth, sw, sh);
    }

    private static void renderBar(GuiGraphics graphics, float healthPercent, int sw, int sh) {
        int barWidth = 182;
        int barHeight = 12;

        int marginBottom = 48;
        int progressWidth = Math.round(barWidth * healthPercent);

        int barX = sw / 2 - barWidth / 2;
        int barY = sh - marginBottom - 9 - 2;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED, HEALTH_BAR_BACKGROUND,
                barX,
                barY,
                barWidth,
                barHeight
        );
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED, HEALTH_BAR_PROGRESS,
                barX,
                barY,
                progressWidth,
                barHeight
        );
    }

    private static void renderText(GuiGraphics graphics, Font font, int currentHealth, int maxHealth, int sw, int sh) {
        Component component = Component.translatable("ui.csc.healthBar", currentHealth, maxHealth).withStyle(ChatFormatting.WHITE);

        int marginBottom = 48;
        int textX = (sw - font.width(component)) / 2;
        int textY = sh - marginBottom - 9;

        graphics.drawString(font, component, textX , textY, 0xFFFFFFFF);
    }
}
