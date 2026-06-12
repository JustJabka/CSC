package justjabka.csc.events.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface OnPlayerHealthChangeCallback {
    Event<OnPlayerHealthChangeCallback> EVENT = EventFactory.createArrayBacked(OnPlayerHealthChangeCallback.class,
            (listeners) -> (player, oldHealth, newHealth) -> {
                for (OnPlayerHealthChangeCallback listener : listeners) {
                    listener.change(player, oldHealth, newHealth);
                }
            }
    );

    void change(Player player, float oldHealth, float newHealth);
}
