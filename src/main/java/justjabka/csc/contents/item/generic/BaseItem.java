package justjabka.csc.contents.item.generic;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public abstract class BaseItem extends Item {
    public static final Identifier BASE_MAX_HEALTH_ID = Identifier.withDefaultNamespace("base_max_health");

    public BaseItem(Properties properties) {
        super(properties);
    }

    // Utils
    protected int getSecondsToTicks(int seconds) {
        return seconds * 20;
    }

    protected String wrapDecimalAsPercent(double value) {
        int percent = Math.toIntExact(Math.round(value * 100));
        return percent + "%";
    }

    protected boolean isClientSide(Player player) {
        return player.level().isClientSide();
    }
}