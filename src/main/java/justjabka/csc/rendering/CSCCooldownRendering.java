package justjabka.csc.rendering;

import justjabka.csc.CSC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class CSCCooldownRendering {
    private static final Identifier COOLDOWN_BACKGROUND = CSC.id("hud/cooldown_background");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        // Get list of all items that on cooldowns
        ItemCooldowns cooldowns = player.getCooldowns();

        int xOffset = 32;

        for (Map.Entry<Identifier, ItemCooldowns.CooldownInstance> entry :  cooldowns.cooldowns.entrySet()) {
            // Get item sprite
            Identifier id = entry.getKey();
            Item item = BuiltInRegistries.ITEM.getValue(id);
            ItemStack stack = item.getDefaultInstance();

            // Calc remaining cooldown
            int now = player.tickCount;
            int remaining = entry.getValue().endTime() - now;

            if (remaining <= 0) continue;
            remaining = remaining / 20;

            // Render
            renderCooldownElement(graphics, font, player, stack, sw, sh, remaining, xOffset);

            xOffset += 32;
        }
    }

    private static void renderCooldownElement(
            GuiGraphicsExtractor graphics,
            Font font,
            Player player,
            ItemStack stack,
            int sw,
            int sh,
            int remaining,
            int xOffset
    ) {
        int baseX = sw - xOffset;
        int baseY = sh - 32;

        int itemSize = 16;
        int itemX = baseX - itemSize / 2;
        int itemY = baseY - itemSize / 2;

        int bgSize = 22;
        int bgX = baseX - bgSize / 2;
        int bgY = baseY - bgSize / 2;

        int textY = baseY + itemSize / 2 + 4;
        Component text = Component
                .translatable("ui.csc.cooldown", remaining)
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                );

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, COOLDOWN_BACKGROUND, bgX, bgY, bgSize, bgSize);
        graphics.item(player, stack, itemX, itemY, 0);
        graphics.itemDecorations(font, stack, itemX, itemY);
        graphics.centeredText(font, text, baseX, textY, -1);
    }
}