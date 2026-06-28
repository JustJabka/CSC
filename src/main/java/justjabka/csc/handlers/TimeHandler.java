package justjabka.csc.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TimeHandler {
    public static final int TICKS_PER_SECOND = 20;

    // Time -> Ticks
    public static int secondsToTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static int minutesToTicks(int minutes) {
        long seconds = Duration.ofMinutes(minutes).toSeconds();
        return secondsToTicks(Math.toIntExact(seconds));
    }

    public static int hoursToTicks(int hours) {
        long seconds = Duration.ofHours(hours).toSeconds();
        return secondsToTicks(Math.toIntExact(seconds));
    }

    // Ticks -> Time
    public static int ticksToSeconds(int ticks) {
        return ticks / TICKS_PER_SECOND;
    }

    public static Component autoConvertTicks(int ticks) {
        if (ticks <= 0) return Component.translatable("other.csc.time.seconds", 0);

        Duration duration = Duration.ofSeconds(ticksToSeconds(ticks));

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        List<Component> parts = new ArrayList<>();

        if (hours > 0) parts.add(Component.translatable("other.csc.time.hours", hours));
        if (minutes > 0) parts.add(Component.translatable("other.csc.time.minutes", minutes));
        if (seconds > 0 || parts.isEmpty()) parts.add(Component.translatable("other.csc.time.seconds", seconds));

        return ComponentUtils.formatList(parts, Component.literal(" "));
    }
}
