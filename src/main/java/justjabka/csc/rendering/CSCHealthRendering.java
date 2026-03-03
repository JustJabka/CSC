package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
        float healthPercent = currentHealth / (float) maxHealth;

        Component text = Component.translatable("ui.csc.healthBar", currentHealth, maxHealth).withStyle(ChatFormatting.WHITE).withoutShadow();
        if (player.gameMode().isSurvival()) {
            CSCHudRendering.renderBar(
                    graphics,
                    font,
                    HEALTH_BAR_BACKGROUND,
                    HEALTH_BAR_PROGRESS,
                    healthPercent,
                    text,
                    sw,
                    sh,
                    0,
                    38,
                    182,
                    12
            );
        }
    }
}
