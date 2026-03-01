package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.registries.CSCAttachments;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CSCHudRendering {
    public static void initialize() {
        // Remove all vanilla HUD elements
        removeVanillaHudElements();

        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing.
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud"), CSCHudRendering::render);
    }

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

    private static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) return;

        int sw = minecraft.getWindow().getGuiScaledWidth();
        int sh = minecraft.getWindow().getGuiScaledHeight();

        renderHealth(graphics, minecraft, player, sw, sh);
        renderArmor(graphics, minecraft, player, sw, sh);
        renderCurrentGold(graphics, minecraft, player, sw, sh);
    }

    private static void renderHealth(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        int currentHealth = Math.round(player.getHealth());
        int maxHealth = Math.round(player.getMaxHealth());

        Component healthBarText = Component.translatable("ui.csc.healthBar", currentHealth, maxHealth).withStyle(ChatFormatting.WHITE);
        graphics.drawString(minecraft.font, healthBarText, sw / 2, sh / 2, 0xFFFFFFFF);
    }

    private static void renderArmor(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        int currentArmor = player.getArmorValue();
        int maxArmor = 20;
        int armorPercent = (currentArmor * 100) / maxArmor;

        Component armorBarText = Component.translatable("ui.csc.armorBar", armorPercent).withStyle(ChatFormatting.WHITE);
        graphics.drawString(minecraft.font, armorBarText, sw / 2 + 100, sh / 2, 0xFFFFFFFF);
    }
    private static void renderCurrentGold(GuiGraphics graphics, Minecraft minecraft, Player player, int sw, int sh) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        int currentGold = data.getGold();

        Component currentGoldText = Component.translatable("ui.csc.currentGold", currentGold).withStyle(ChatFormatting.WHITE);
        graphics.drawString(minecraft.font, currentGoldText, sw / 2 + 200, sh / 2, 0xFFFFFFFF);
    }
}