package justjabka.csc.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.Team;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GameStateHandler {

    private GameStateHandler() {}

    private static final Map<Team, TeamState> TEAM_STATES = new ConcurrentHashMap<>();

    private static final int DUEL_DURATION = 300 * 20;
    private static final int HUB_DURATION = 30 * 20;

    private static int wave = 0;
    private static GameState state = GameState.START;

    private static int waveTimer = 0;
    private static int hubTimer = 0;

    public static GameState getGameState() {
        return state;
    }

    public static void startHub(MinecraftServer server) {
        state = GameState.HUB;
        hubTimer = HUB_DURATION;

        wave++;

        server.sendSystemMessage(Component.literal("hub start"));

        BarHandler.updateBar(server, Component.translatable("ui.csc.wave", wave, "Test"), HUB_DURATION, hubTimer, true);
    }

    public static void startActive(MinecraftServer server) {
        state = GameState.ACTIVE;
        waveTimer = WaveState.getTotalDuration();

        // TODO: add logic for duels

        server.sendSystemMessage(Component.literal("wave start"));
        BarHandler.updateBar(server, Component.translatable("ui.csc.wave", wave, "Some WaveState"), waveTimer, waveTimer, true);
    }

    public static void tick(MinecraftServer server) {
        if (state == GameState.HUB) hubTick(server);
        else if (state == GameState.ACTIVE) activeTick(server);
    }

    private static void hubTick(MinecraftServer server) {
        hubTimer--;
        BarHandler.updateBar(server, Component.translatable("ui.csc.wave", wave, "Test"), HUB_DURATION, hubTimer, true);

        if (hubTimer <= 0) {
            server.sendSystemMessage(Component.literal("hub end"));
            startActive(server);
        }
    }

    private static void activeTick(MinecraftServer server) {
        waveTimer--;

        if (waveTimer <= 0) {
            server.sendSystemMessage(Component.literal("wave end"));
            startHub(server);
        }

        BarHandler.updateBar(server, Component.translatable("ui.csc.wave", wave, "Some WaveState"), WaveState.getTotalDuration(), waveTimer, true);
    }

    public enum GameState {
        START,
        ACTIVE,
        HUB,
        END;
    }

    public enum TeamState {
        WAVE,
        HUB,
        DUEL,
        SPECTATOR,
        DEAD;
    }

    public enum WaveState {
        NORMAL(1000),
        BUFF(1000),
        DAMAGE_REFLECTION(1000),
        PLAYER_DEATH(1000);

        private final int duration;

        WaveState(int duration) {
            this.duration = duration;
        }

        public int getDuration() {
            return this.duration;
        }

        public static int getTotalDuration() {
            int duration = 0;

            for (WaveState state : WaveState.values()) {
                duration += state.duration;
            }

            return duration;
        }
    }
}
