package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class CSCMagicalResistanceRendering {
    private static final Identifier ICON_FONT = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "icons");
    private static final Identifier MAGICAL_RESISTANCE_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/magical_resistance_bar_background");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        AttributeInstance magicalResistanceInstance = player.getAttribute(CSCAttributes.MAGIC_RESISTANCE);
        double magicalResistance = magicalResistanceInstance == null ? 0 : magicalResistanceInstance.getValue();
        int magicalResistancePercent = Math.toIntExact(Math.round(magicalResistance * 100));

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
                .translatable("ui.csc.magicalResistance", magicalResistancePercent)
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
                MAGICAL_RESISTANCE_BAR_BACKGROUND,
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