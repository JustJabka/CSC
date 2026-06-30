package justjabka.csc.events;

import justjabka.csc.handlers.ShopHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
