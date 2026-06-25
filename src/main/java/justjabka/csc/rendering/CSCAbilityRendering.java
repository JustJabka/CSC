package justjabka.csc.rendering;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.concurrent.atomic.AtomicInteger;

public class CSCAbilityRendering {
    private static final Identifier ABILITY_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/ability_bar_background");
    private static final Identifier ABILITY_BAR_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/ability_bar_progress");

    public static void render(GuiGraphicsExtractor graphics, Font font, Player player, int sw, int sh) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        AtomicInteger yOffset = new AtomicInteger(0);
        int yOffsetStep = 16;

        data.abilities().forEach((key, abilityData) -> {
            Identifier icon = abilityData.icon();
            int duration = abilityData.duration();
            int maxDuration = abilityData.maxDuration();

            int remainingSeconds = TimeHandler.ticksToSeconds(duration);
            float progressPercent = maxDuration > 0 ? (float) duration / maxDuration : 1f;

            renderAbility(graphics, font, player, sw, sh, icon, remainingSeconds, progressPercent, yOffset.get());

            yOffset.addAndGet(yOffsetStep);
        });
    }

    public static void renderAbility(
            GuiGraphicsExtractor graphics,
            Font font,
            Player player,
            int sw,
            int sh,
            Identifier icon,
            int remainingSeconds,
            float progressPercent,
            int yOffset
    ) {
        int baseX = 0;
        int baseY = 52 + yOffset;
        int width = 86;
        int itemSize = 16;

        int itemX = sw / 2 - width / 2 + baseX - itemSize;
        int itemY = sh - baseY - itemSize;

        Item item = BuiltInRegistries.ITEM.getValue(icon);
        Component text = Component
                .translatable("ui.csc.abilityBar", remainingSeconds)
                .withStyle(style -> style
                        .withColor(ChatFormatting.WHITE)
                );

        CSCHudRendering.renderBar(
                graphics,
                font,
                ABILITY_BAR_BACKGROUND,
                ABILITY_BAR_PROGRESS,
                progressPercent,
                text,
                sw,
                sh,
                baseX,
                baseY,
                width,
                12
        );

        graphics.item(player, item.getDefaultInstance(), itemX, itemY, 0);
    }
}