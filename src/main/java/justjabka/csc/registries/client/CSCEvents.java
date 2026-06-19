package justjabka.csc.registries.client;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;

public class CSCEvents {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Client Events");

        // Prevent item drop in opened inventory
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenKeyboardEvents.allowKeyPress(screen).register((scr, event) ->
                    !client.options.keyDrop.matches(event));
        });
    }
}
