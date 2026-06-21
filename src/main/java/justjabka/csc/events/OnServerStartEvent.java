package justjabka.csc.events;

import justjabka.csc.handlers.ShopHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class OnServerStartEvent {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(OnServerStartEvent::initCache);
    }

    private static void initCache(MinecraftServer server) {
        ShopHandler.initCache();
    }
}
