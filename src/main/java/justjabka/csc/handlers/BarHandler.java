package justjabka.csc.handlers;

import justjabka.csc.CSC;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import org.jspecify.annotations.Nullable;

public final class BarHandler {

    private BarHandler() {}

    public static final Identifier BAR = CSC.key("bar");

    public static @Nullable CustomBossEvent getBar(MinecraftServer server) {
        CustomBossEvents events = server.getCustomBossEvents();

        return events.get(BAR);
    }

    public static void updateBar(MinecraftServer server, Component name, int max, int value, boolean visible) {
        CustomBossEvent bar = getBar(server);
        if (bar == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            bar.addPlayer(player);
        }

        bar.setName(name);
        bar.setMax(max);
        bar.setValue(value);
        bar.setVisible(visible);
    }

    public static void initBar(MinecraftServer server) {
        CustomBossEvents events = server.getCustomBossEvents();

        if (events.get(BAR) != null) return;

        final Component name = Component.empty();
        final BossEvent.BossBarColor color = BossEvent.BossBarColor.BLUE;

        CustomBossEvent bar = events.create(RandomSource.create(), BAR, name);

        bar.setColor(color);
    }
}
