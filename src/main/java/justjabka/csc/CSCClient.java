package justjabka.csc;

import justjabka.csc.registries.client.CSCKeyMappings;
import justjabka.csc.registries.client.CSCScreens;
import justjabka.csc.rendering.CSCHudRendering;
import net.fabricmc.api.ClientModInitializer;

public class CSCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CSCHudRendering.initialize();
        CSCKeyMappings.initialize();
        CSCScreens.initialize();
    }
}
