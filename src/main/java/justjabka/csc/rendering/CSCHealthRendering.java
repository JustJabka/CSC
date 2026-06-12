package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class CSCHealthRendering {
    private static final Identifier ICON_FONT = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "icons");
    private static final Identifier HEALTH_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_background");
    private static final Identifier HEALTH_BAR_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_progress");
    private static final Identifier HEALTH_BAR_ABSORPTION_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_absorption_progress");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        int currentHealth = Math.round(player.getHealth());
        int maxHealth = Math.round(player.getMaxHealth());
        float healthPercent = currentHealth / (float) maxHealth;

        int currentAbsorptionAmount = Math.round(player.getAbsorptionAmount());
        boolean hasAbsorption = currentAbsorptionAmount > 0;

        currentHealth = hasAbsorption ? currentHealth + currentAbsorptionAmount : currentHealth;
        Identifier barProgress = hasAbsorption ? HEALTH_BAR_ABSORPTION_PROGRESS : HEALTH_BAR_PROGRESS;

        renderBar(graphics, font, player, sw, sh, currentHealth, maxHealth, healthPercent, barProgress);
    }

    private static void renderBar(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh, int currentHealth, int maxHealth, float healthPercent, Identifier barProgress) {
        Component icon = Component
                .literal("❤")
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withoutShadow()
                        .withFont(new FontDescription.Resource(ICON_FONT))
                );
        Component info = Component
                .translatable("ui.csc.healthBar", currentHealth, maxHealth)
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withoutShadow()
                );
        Component text = Component
                .empty()
                .append(icon)
                .append(info);

        if (!showHealthBar(player)) return;

        CSCHudRendering.renderBar(
                graphics,
                font,
                HEALTH_BAR_BACKGROUND,
                barProgress,
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

    private static boolean showHealthBar(Player player) {
        GameType gameMode = player.gameMode();

        if (gameMode == null) return false;
        return gameMode.isSurvival();
    }
}
