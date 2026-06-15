package justjabka.csc.handlers;

import net.minecraft.network.chat.Component;

public class TimeHandler {
    public static final int TICKS_PER_SECOND = 20;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;

    // ... -> Ticks
    public static int secondsToTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static int minutesToTicks(int minutes) {
        return minutes * SECONDS_PER_MINUTE * TICKS_PER_SECOND;
    }

    public static int hoursToTicks(int hours) {
        return hours * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * TICKS_PER_SECOND;
    }

    // ... -> ...
    public static int ticksToSeconds(int ticks) {
        return ticks / TICKS_PER_SECOND;
    }

    public static int secondsToMinutes(int seconds) {
        return seconds / SECONDS_PER_MINUTE;
    }

    public static int minutesToHours(int minutes) {
        return minutes / MINUTES_PER_HOUR;
    }

    public static Component autoConvertTicks(int ticks) {
        int totalSeconds = ticksToSeconds(ticks);

        if (totalSeconds < SECONDS_PER_MINUTE) {
            return Component.translatable("other.csc.time.seconds", totalSeconds);
        }

        int totalMinutes = secondsToMinutes(totalSeconds);

        if (totalMinutes < MINUTES_PER_HOUR) {
            return Component.translatable("other.csc.time.minutes", totalMinutes);
        }

        int totalHours = minutesToHours(totalMinutes);

        return Component.translatable("other.csc.time.hours", totalHours);
    }
}
