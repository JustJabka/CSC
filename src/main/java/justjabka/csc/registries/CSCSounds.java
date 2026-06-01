package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class CSCSounds {
    // Item
    public static final SoundEvent ITEM_IN_COOLDOWN = registerSound("item.in_cooldown");
    public static final SoundEvent ITEM_MIDAS = registerSound("item.midas.use");
    public static final SoundEvent ITEM_PHOENIX_FEATHER = registerSound("item.phoenix_feather.use");
    public static final SoundEvent ITEM_THORNS = registerSound("item.thorns.use");
    public static final SoundEvent ITEM_MAGIC_PROTECTION_CLOCK = registerSound("item.magic_protection_clock.use");
    public static final SoundEvent ITEM_DARK_GAUNTLET_ACTIVATE = registerSound("item.dark_gauntlet.activate");
    public static final SoundEvent ITEM_DARK_GAUNTLET_DEACTIVATE = registerSound("item.dark_gauntlet.deactivate");

    // Entity
    public static final SoundEvent PLAYER_DODGE = registerSound("entity.player.dodge");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CSC.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize() {
        CSC.LOGGER.info("Initializing Sounds");
    }
}