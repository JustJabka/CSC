package justjabka.csc.events;

import justjabka.csc.registries.CSCAttachments;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class OnServerTickEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER).tick();
            }
        });
    }
}
