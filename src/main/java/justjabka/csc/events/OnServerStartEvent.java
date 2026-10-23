package justjabka.csc.events;

import justjabka.csc.CSC;
import justjabka.csc.handlers.ShopHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public class OnServerStartEvent {
    public static void register() {
        Event<ServerLifecycleEvents.ServerStarted> serverStarted = ServerLifecycleEvents.SERVER_STARTED;

        serverStarted.register(OnServerStartEvent::initGameRules);
        serverStarted.register(OnServerStartEvent::initCache);
        serverStarted.register(OnServerStartEvent::initTeams);
        serverStarted.register(OnServerStartEvent::initBossbar);
    }

    private static void initCache(MinecraftServer server) {
        ShopHandler.initCache();
    }

    private static void initGameRules(MinecraftServer server) {
        GameRules gameRules = server.getGameRules();

        gameRules.set(GameRules.ADVANCE_TIME, false, server);
        gameRules.set(GameRules.ADVANCE_WEATHER, false, server);

        gameRules.set(GameRules.SPAWN_MOBS, false, server);

        gameRules.set(GameRules.BLOCK_DROPS, false, server);
        gameRules.set(GameRules.MOB_GRIEFING, false, server);
        gameRules.set(GameRules.TNT_EXPLODES, false, server);

        gameRules.set(GameRules.KEEP_INVENTORY, true, server);
        gameRules.set(GameRules.LOCATOR_BAR, false, server);

        gameRules.set(GameRules.SHOW_DEATH_MESSAGES, false, server);
        gameRules.set(GameRules.SHOW_ADVANCEMENT_MESSAGES, false, server);
    }

    private static void initTeams(MinecraftServer server) {
        createTeam(server, ChatFormatting.RED);
        createTeam(server, ChatFormatting.GREEN);
        createTeam(server, ChatFormatting.BLUE);
        createTeam(server, ChatFormatting.YELLOW);
        createTeam(server, ChatFormatting.LIGHT_PURPLE);
        createTeam(server, ChatFormatting.AQUA);
        createTeam(server, ChatFormatting.WHITE);
        createTeam(server, ChatFormatting.BLACK);
        createTeam(server, ChatFormatting.GRAY);
    }

    private static void initBossbar(MinecraftServer server) {
        CustomBossEvents events = server.getCustomBossEvents();

        Identifier id = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "bar");

        if (events.get(id) != null) return;

        final Component name = Component.literal("Bar");
        final BossEvent.BossBarColor color = BossEvent.BossBarColor.BLUE;

        CustomBossEvent bar = events.create(RandomSource.create(), id, name);

        bar.setColor(color);
    }

    private static void createTeam(MinecraftServer server, final ChatFormatting color) {
        String name = color.getName();
        String displayNameKey = "color.minecraft.%s".formatted(name);
        Component displayName = Component.translatable(displayNameKey);

        Scoreboard scoreboard = server.getScoreboard();

        if (scoreboard.getPlayerTeam(name) != null) return;
        PlayerTeam team = scoreboard.addPlayerTeam(name);

        team.setDisplayName(displayName);
        team.setColor(color);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        team.setAllowFriendlyFire(false);
        team.setSeeFriendlyInvisibles(true);
    }
}
