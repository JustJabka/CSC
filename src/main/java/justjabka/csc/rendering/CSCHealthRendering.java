package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCHealthRendering {
//    private static final Identifier ICON = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_icon");
    private static final Identifier ICON_FONT = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "icons");
    private static final Identifier HEALTH_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_background");
    private static final Identifier HEALTH_BAR_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/health_bar_progress");

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        int currentHealth = Math.round(player.getHealth());
        int maxHealth = Math.round(player.getMaxHealth());
        float healthPercent = currentHealth / (float) maxHealth;

//        Component icon = Component
//                .object(new AtlasSprite(AtlasIds.GUI, ICON))
//                .withStyle(style -> style
//                        .withColor(ChatFormatting.WHITE)
//                        .withoutShadow()
//                );
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
