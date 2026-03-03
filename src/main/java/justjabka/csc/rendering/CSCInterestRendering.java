package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCInterestRendering {
    // Define Textures
    private static final Identifier INTEREST_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/interest_bar_background");

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        int interest = 0; // Hardcoded for now because PVP haven't implemented yet

        int width = 46;
        int height = 12;
        Component text = Component.translatable("ui.csc.interestBar", interest).withStyle(ChatFormatting.WHITE).withoutShadow();

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