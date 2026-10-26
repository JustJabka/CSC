package justjabka.csc.types;

import org.apache.commons.lang3.Range;

// TODO: add proper scale for later waves
public enum WaveScaleType {
    EARLY_GAME(Range.of(1, 10), 0.2),
    MID_GAME(Range.of(11, 99), 0.1),
    LATE_GAME(Range.of(100, Integer.MAX_VALUE), 0.01);

    private final Range<Integer> startingWave;
    private final double attributeMultiplier;

    public Range<Integer> getStartingWave() {
        return startingWave;
    }

    public double getAttributeMultiplier() {
        return attributeMultiplier;
    }

    WaveScaleType(Range<Integer> startingWave, double attributeMultiplier) {
        this.startingWave = startingWave;
        this.attributeMultiplier = attributeMultiplier;
    }

    public static WaveScaleType getFromWave(int wave) {
        for (WaveScaleType type : WaveScaleType.values()) {
            if (!type.startingWave.contains(wave)) continue;
            return type;
        }

        return LATE_GAME;
    }
}