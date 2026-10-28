package justjabka.csc.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.TeamColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TeamHandler {

    private TeamHandler() {}

    public static boolean isFriendlyFire(LivingEntity entity1, LivingEntity entity2) {
        return entity1 == entity2 || entity1.isAlliedTo(entity2);
    }

    public static List<LivingEntity> getTeammatesInRadius(LivingEntity entity, double radius) {
        final AABB searchBox = entity.getBoundingBox().inflate(radius);

        return entity.level().getEntitiesOfClass(LivingEntity.class, searchBox, target -> {
            if (!target.isAlive()) return false;
            if (target.isSpectator()) return false;

            return target.isAlliedTo(entity);
        });
    }

    public static List<Player> getPlayerTeammatesInRadius(Player entity, double radius) {
        final List<Player> players = new ArrayList<>();

        for (LivingEntity e : getTeammatesInRadius(entity, radius)) {
            if (!(e instanceof Player player)) continue;
            players.add(player);
        }

        return players;
    }

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
        String name = color.name().toLowerCase();
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
