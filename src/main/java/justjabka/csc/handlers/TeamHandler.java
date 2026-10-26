package justjabka.csc.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public final class TeamHandler {

    private TeamHandler() {}

    public static void initTeams(MinecraftServer server) {
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
