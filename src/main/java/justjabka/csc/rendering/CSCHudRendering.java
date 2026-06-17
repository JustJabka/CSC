package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class CSCHudRendering {
    public static void removeVanillaHudElements() {
        // Base Stats
        HudElementRegistry.removeElement(VanillaHudElements.AIR_BAR);
        HudElementRegistry.removeElement(VanillaHudElements.ARMOR_BAR);
        HudElementRegistry.removeElement(VanillaHudElements.FOOD_BAR);
        HudElementRegistry.removeElement(VanillaHudElements.HEALTH_BAR);

        // XP Bar
        HudElementRegistry.removeElement(VanillaHudElements.INFO_BAR);
        HudElementRegistry.removeElement(VanillaHudElements.EXPERIENCE_LEVEL);
    }

    public static void initialize() {
        CSC.LOGGER.info("Initializing HUD Rendering");

        // Remove all vanilla HUD elements
        removeVanillaHudElements();

        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing.
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud"), CSCHudRendering::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker tickCounter) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Font font = minecraft.font;

        if (player == null) return;

        int sw = minecraft.getWindow().getGuiScaledWidth();
        int sh = minecraft.getWindow().getGuiScaledHeight();

        // TODO: Add attributes in hud
        CSCHealthRendering.render(graphics, font, player, sw, sh);
        CSCMagicalResistanceRendering.render(graphics, font, player, sw, sh);
        CSCGoldRendering.render(graphics, font, player, sw, sh);
        CSCArmorRendering.render(graphics, font, player, sw, sh);
        CSCCooldownRendering.render(graphics, font, player, sw, sh);
        CSCAbilityRendering.render(graphics, font, player, sw, sh);
    }

    public static void renderBar(
            GuiGraphicsExtractor graphics,
            Font font,
            Identifier background,
            Identifier progress,
            float percent,
            Component text,
            int sw,
            int sh,
            int centerOffsetX,
            int bottomOffset,
            int width,
            int height
    ) {
        percent = Mth.clamp(percent, 0f, 1f);
        int progressWidth = Mth.lerpDiscrete(percent, 0, width);

        int barX = sw / 2 - width / 2 + centerOffsetX;
        int barY = sh - bottomOffset - height;

        // Background
        if (background != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, barX, barY, width, height);
        }

        // Progress
        if (progress != null && progressWidth > 0) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    progress,
                    width,
                    height,
                    0,
                    0,
                    barX,
                    barY,
                    progressWidth,
                    height
                    -1
            );
        }

        // Center text inside bar
        if (text != null) {
            int textX = barX + (width - font.width(text)) / 2;
            int textY = (barY + (height - font.lineHeight) / 2) + 1;

            graphics.text(font, text, textX, textY, -1);
        }
    }
}