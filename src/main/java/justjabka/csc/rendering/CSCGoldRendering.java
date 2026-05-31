package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCGoldRendering {
    private static final Identifier ICON_FONT = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "icons");
    private static final Identifier GOLD_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/gold_bar_background");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        int gold = data.gold();

        Component icon = Component
                .literal("♦")
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                        .withoutShadow()
                        .withFont(new FontDescription.Resource(ICON_FONT))
                );
        Component info = Component
                .translatable("ui.csc.goldBar", gold)
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
