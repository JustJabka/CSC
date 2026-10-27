package justjabka.csc.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.TeamColor;

import java.util.Optional;

public final class TeamHandler {

    private TeamHandler() {}

    public static void initTeams(MinecraftServer server) {
        createTeam(server, TeamColor.RED);
        createTeam(server, TeamColor.GREEN);
        createTeam(server, TeamColor.BLUE);
        createTeam(server, TeamColor.YELLOW);
        createTeam(server, TeamColor.LIGHT_PURPLE);
        createTeam(server, TeamColor.AQUA);
        createTeam(server, TeamColor.WHITE);
        createTeam(server, TeamColor.BLACK);
        createTeam(server, TeamColor.GRAY);
    }

    private static void createTeam(MinecraftServer server, final TeamColor color) {
        String name = color.name();
        String displayNameKey = "color.minecraft.%s".formatted(name);
        Component displayName = Component.translatable(displayNameKey);

        Scoreboard scoreboard = server.getScoreboard();

        if (scoreboard.getPlayerTeam(name) != null) return;
        PlayerTeam team = scoreboard.addPlayerTeam(name);

        team.setDisplayName(displayName);
        team.setColor(Optional.of(color));
        team.setCollisionRule(Team.CollisionRule.NEVER);
        team.setAllowFriendlyFire(false);
        team.setSeeFriendlyInvisibles(true);
    }
}
