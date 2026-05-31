package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCInterestRendering {
    private static final Identifier ICON_FONT = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "icons");
    private static final Identifier INTEREST_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/interest_bar_background");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        int interest = 0; // Hardcoded for now because PVP haven't implemented yet

        int width = 46;
        int height = 12;

        Component icon = Component
                .literal("♣")
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withoutShadow()
                        .withFont(new FontDescription.Resource(ICON_FONT))
                );
        Component info = Component
                .translatable("ui.csc.interestBar", interest)
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withoutShadow()
                );
        Component text = Component
                .empty()
                .append(icon)
                .append(info);

        CSCHudRendering.renderBar(
                graphics,
                font,
                INTEREST_BAR_BACKGROUND,
                null,
                0f,
                text,
                sw,
                sh,
                -91 + width / 2,
                24,
                width,
                height
        );
    }
}