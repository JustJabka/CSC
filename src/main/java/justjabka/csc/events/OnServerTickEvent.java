package justjabka.csc.events;

import justjabka.csc.handlers.AbilityHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class OnServerTickEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(OnServerTickEvent::tickAbilities);
    }

    private static void tickAbilities(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            AbilityHandler.tick(player);
        }
    }
}
