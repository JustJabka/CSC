package justjabka.csc.handlers;

public class ActiveItemConfig {
    public final boolean haveCooldown;
    public final int cooldown;
    public final int duration;

    public ActiveItemConfig(
            boolean haveCooldown,
            int cooldown,
            int duration
    ) {
        this.haveCooldown = haveCooldown;
        this.cooldown = cooldown;
        this.duration = duration;
    }
}