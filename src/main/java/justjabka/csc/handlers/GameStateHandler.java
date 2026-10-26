package justjabka.csc.handlers;

import justjabka.csc.types.WaveState;
import justjabka.csc.types.WaveType;
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

    private static GameState state = GameState.START;

    private static int wave = 0;
    private static WaveType waveType = null;
    private static WaveState waveState = null;

    private static int waveTimer = 0;
    private static int hubTimer = 0;

    public static GameState getGameState() {
        return state;
    }

    public static void startHub(MinecraftServer server) {
        state = GameState.HUB;
        hubTimer = HUB_DURATION;

        wave++;
        waveType = WaveType.getRandomWaveType();

        updateBarHub(server, waveType, hubTimer);

        if (WaveHandler.hasItemSelection(wave)) {
            ItemSelectionHandler.openMenu(server.getPlayerList().getPlayers());
        }
    }

    public static void startActive(MinecraftServer server) {
        state = GameState.ACTIVE;
        waveTimer = WaveState.getTotalDuration();
        waveState = WaveState.NORMAL;

        updateBarActive(server, waveState, waveTimer);

        // TODO: add logic for duels
        // TODO: add logic for wave spawn
    }

    public static void tick(MinecraftServer server) {
        if (state == GameState.HUB) hubTick(server);
        else if (state == GameState.ACTIVE) activeTick(server);
    }

    private static void hubTick(MinecraftServer server) {
        hubTimer--;
        updateBarHub(server, waveType, hubTimer);

        if (hubTimer <= 0) {
            startActive(server);
        }
    }

    private static void activeTick(MinecraftServer server) {
        waveTimer--;

        if (waveTimer <= 0) {
            startHub(server);
        }

        updateBarActive(server, waveState, waveTimer);
    }

    private static void updateBarActive(MinecraftServer server, WaveState waveState, int value) {
        final Component name = Component.translatable("ui.csc.wave", wave, waveState.getTranslate());
        BarHandler.updateBar(server, name, WaveState.getTotalDuration(), value, true);
    }
    
    private static void updateBarHub(MinecraftServer server, WaveType waveType, int value) {
        final Component name = Component.translatable("ui.csc.wave", wave, waveType.getTranslate());
        BarHandler.updateBar(server, name, HUB_DURATION, value, true);
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
}
