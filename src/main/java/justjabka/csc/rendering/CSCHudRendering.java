package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

    private static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Font font = minecraft.font;

        if (player == null) return;

        int sw = minecraft.getWindow().getGuiScaledWidth();
        int sh = minecraft.getWindow().getGuiScaledHeight();

        CSCHealthRendering.render(graphics, font, player, sw, sh);
        renderGoldFromInterest(graphics, minecraft, player, sw, sh);
        renderGold(graphics, minecraft, player, sw, sh);
        renderArmor(graphics, minecraft, player, sw, sh);
    }

    private static void renderGoldFromInterest(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        int goldFromInterest = 0; // Hardcoded for now because PVP haven't implemented yet

        Component textComponent = Component.translatable("ui.csc.goldBar", goldFromInterest).withStyle(ChatFormatting.GREEN);
        graphics.drawString(minecraft.font, textComponent, sw / 2 + 50, sh / 2, 0xFFFFFFFF);
    }

    private static void renderGold(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        int gold = data.gold();

        Component textComponent = Component.translatable("ui.csc.goldBar", gold).withStyle(ChatFormatting.YELLOW);
        graphics.drawString(minecraft.font, textComponent, sw / 2 + 100, sh / 2, 0xFFFFFFFF);
    }

    private static void renderArmor(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        int currentArmor = player.getArmorValue();
        int maxArmor = 20;
        int armorPercent = (currentArmor * 100) / maxArmor;

        Component textComponent = Component.translatable("ui.csc.armorBar", armorPercent).withStyle(ChatFormatting.GRAY);
        graphics.drawString(minecraft.font, textComponent, sw / 2 + 150, sh / 2, 0xFFFFFFFF);
    }
}