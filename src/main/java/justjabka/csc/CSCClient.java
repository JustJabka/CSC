package justjabka.csc;

import justjabka.csc.rendering.CSCHudRendering;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;

public class CSCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CSCHudRendering.initialize();

        // Prevent item drop in opened inventory
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenKeyboardEvents.allowKeyPress(screen).register((scr, event) ->
                    !client.options.keyDrop.matches(event));
        });
    }
}
