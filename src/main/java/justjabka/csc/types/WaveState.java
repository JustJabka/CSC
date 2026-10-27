package justjabka.csc.types;

import justjabka.csc.CSC;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum WaveState implements StringRepresentable {
    NORMAL("normal", 1000),
    BUFF("buff", 1000),
    DAMAGE_REFLECTION("damage_reflection", 1000),
    PLAYER_DEATH("player_death", 1000);

    private final String name;
    private final int duration;

    WaveState(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public @NonNull Component getTranslate() {
        return Component.translatable("ui.%s.wave.state.%s".formatted(CSC.MOD_ID, getName()));
    }

    public int getDuration() {
        return this.duration;
    }

    public static WaveState getStateFromTimer(int timer) {
        final int elapsed = getTotalDuration() - timer;

        int accumulated = 0;
        for (WaveState state : WaveState.values()) {
            accumulated += state.getDuration();

            if (elapsed >= accumulated) continue;

            return state;
        }

        return WaveState.PLAYER_DEATH;
    }

    public static int getTotalDuration() {
        int duration = 0;

        for (WaveState state : WaveState.values()) {
            duration += state.duration;
        }

        return duration;
    }
}
