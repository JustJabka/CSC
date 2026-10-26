package justjabka.csc.events;

import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.GameStateHandler;
import justjabka.csc.handlers.HealthRegenerationHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class OnServerTickEvent {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(OnServerTickEvent::tick);
    }

    private static void tick(MinecraftServer server) {
        GameStateHandler.tick(server);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            AbilityHandler.tick(player);
            HealthRegenerationHandler.tick(player);
        }
    }
}
