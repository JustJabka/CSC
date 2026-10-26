package justjabka.csc.types;

import org.apache.commons.lang3.Range;

// TODO: add proper scale for later waves
public enum WaveScaleType {
    EARLY_GAME(Range.of(1, 10), 0.2),
    MID_GAME(Range.of(11, 99), 0.1),
    LATE_GAME(Range.of(100, Integer.MAX_VALUE), 0.01);

    private final Range<Integer> waves;
    private final double attributeMultiplier;

    public Range<Integer> getWaves() {
        return waves;
    }

    public double getAttributeMultiplier() {
        return attributeMultiplier;
    }

    WaveScaleType(Range<Integer> waves, double attributeMultiplier) {
        this.waves = waves;
        this.attributeMultiplier = attributeMultiplier;
    }

    public static WaveScaleType getFromWave(int wave) {
        for (WaveScaleType type : WaveScaleType.values()) {
            if (!type.waves.contains(wave)) continue;
            return type;
        }

        return LATE_GAME;
    }
}