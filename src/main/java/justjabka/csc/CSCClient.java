package justjabka.csc;

import justjabka.csc.rendering.CSCHudRendering;
import net.fabricmc.api.ClientModInitializer;

public class CSCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CSCHudRendering.initialize();
    }
}
