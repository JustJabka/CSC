package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCArmorRendering {
    // Define Textures
    private static final Identifier ARMOR_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/armor_bar_background");

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        int maxArmor = 20;
        int currentArmor = player.getArmorValue();
        int armorPercent = (currentArmor * 100) / maxArmor;

        int width = 46;
        int height = 12;

        Component text = Component.translatable("ui.csc.armorBar", armorPercent).withStyle(ChatFormatting.WHITE).withoutShadow();

        CSCHudRendering.renderBar(
                graphics,
                font,
                ARMOR_BAR_BACKGROUND,
                null,
                armorPercent,
                text,
                sw,
                sh,
                91 - width / 2,
                24,
                width,
                height
        );
    }
}
