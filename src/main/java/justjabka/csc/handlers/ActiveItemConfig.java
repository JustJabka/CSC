package justjabka.csc.handlers;

public class ActiveItemConfig {
    public final boolean haveCooldown;
    public final boolean haveAbility;
    public final int cooldown;
    public final int duration;

    public ActiveItemConfig(
            boolean haveCooldown,
            boolean haveAbility,
            int cooldown,
            int duration
    ) {
        this.haveCooldown = haveCooldown;
        this.haveAbility = haveAbility;
        this.cooldown = cooldown;
        this.duration = duration;
    }
}