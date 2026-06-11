package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.events.OnDeathEvent;
import justjabka.csc.events.OnServerTickEvent;

public class CSCEvents {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Events");

        OnServerTickEvent.register();
        OnDeathEvent.register();
    }
}
