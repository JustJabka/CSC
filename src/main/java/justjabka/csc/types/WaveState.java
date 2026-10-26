package justjabka.csc.types;

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
