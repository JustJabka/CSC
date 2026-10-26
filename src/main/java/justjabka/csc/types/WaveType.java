package justjabka.csc.types;

import justjabka.csc.CSC;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Random;

public enum WaveType implements StringRepresentable {
    ZOMBIES("zombies"),
    SPIDERS("spiders"),
    CAVE_SPIDERS("cave_spiders"),
    SKELETONS("skeletons");

    private final String name;

    private static final List<WaveType> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static final StringRepresentable.EnumCodec<WaveType> CODEC = StringRepresentable.fromEnum(WaveType::values);

    WaveType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public @NonNull Component getTranslate() {
        return Component.translatable("ui.%s.wave.%s".formatted(CSC.MOD_ID, getName()));
    }

    public static WaveType getRandomWaveType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
