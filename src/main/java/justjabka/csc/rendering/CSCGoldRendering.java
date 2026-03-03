package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCGoldRendering {
    // Define Textures
    private static final Identifier GOLD_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/gold_bar_background");

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        int gold = data.gold();

        Component text = Component.translatable("ui.csc.goldBar", gold).withStyle(ChatFormatting.WHITE).withoutShadow();

        CSCHudRendering.renderBar(
                graphics,
                font,
                GOLD_BAR_BACKGROUND,
                null,
                0f,
                text,
                sw,
                sh,
                0,
                24,
                86,
                12
        );
    }
}
