package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.events.HolyBlanketDeathProtectionEvent;
import justjabka.csc.events.OnServerTickEvent;

public class CSCEvents {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Events");

        OnServerTickEvent.register();
        HolyBlanketDeathProtectionEvent.register();
    }
}
