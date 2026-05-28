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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CSCAbilityRendering {
    private static final Identifier ABILITY_BAR_BACKGROUND = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/ability_bar_background");
    private static final Identifier ABILITY_BAR_PROGRESS = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "hud/ability_bar_progress");

    private static final Map<Identifier, Integer> MAX_DURATION_CACHE = new HashMap<>();

    public static void render(GuiGraphics graphics, Font font, Player player, int sw, int sh) {
        PlayerData data = player.getAttached(CSCAttachments.PLAYER_DATA);
        if (data == null) {
            MAX_DURATION_CACHE.clear();
            return;
        }

        MAX_DURATION_CACHE.keySet().removeIf(key -> !data.abilities().containsKey(key));

        AtomicInteger yOffset = new AtomicInteger(0);
        int yOffsetStep = 16;

        data.abilities().forEach((key, remainingTicks) -> {
            int maxTicks = MAX_DURATION_CACHE.getOrDefault(key, 0);

            if (remainingTicks > maxTicks) {
                maxTicks = remainingTicks;
                MAX_DURATION_CACHE.put(key, maxTicks);
            }

            int remainingSeconds = remainingTicks / 20;
            float progressPercent = maxTicks > 0 ? (float) remainingTicks / maxTicks : 1f;

            renderAbility(graphics, font, player, sw, sh, remainingSeconds, yOffset.get(), progressPercent);

            yOffset.addAndGet(yOffsetStep);
        });
    }

    public static void renderAbility(
            GuiGraphics graphics,
            Font font,
            Player player,
            int sw,
            int sh,
            int remainingSeconds,
            int yOffset,
            float progressPercent
    ) {
        int baseX = 0;
        int baseY = 52 + yOffset;

        Component text = Component
                .literal(String.valueOf(remainingSeconds))
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
                86,
                12
        );
    }
}