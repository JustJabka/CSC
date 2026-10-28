package justjabka.csc.registries.client;

import com.mojang.blaze3d.platform.InputConstants;
import justjabka.csc.CSC;
import justjabka.csc.payloads.ActivateTrinketPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.Nullable;

public class CSCKeyMappings {
    public static final KeyMapping.Category ACTIVATION_CATEGORY = KeyMapping.Category.register(
            CSC.id("activation")
    );

    // Trinkets
    public static KeyMapping faceActivationKey = register("face", InputConstants.KEY_Z);
    public static KeyMapping capeActivationKey = register("cape", InputConstants.KEY_X);
    public static KeyMapping beltActivationKey0 = register("belt", InputConstants.KEY_C, 0);
    public static KeyMapping beltActivationKey1 = register("belt", InputConstants.KEY_V, 1);
    public static KeyMapping agletActivationKey = register("aglet", InputConstants.KEY_G);

    // Abilities
    public static KeyMapping abilityActivationKey0 = register("ability", InputConstants.KEY_R, 0);
    public static KeyMapping abilityActivationKey1 = register("ability", InputConstants.KEY_J, 1);

    public static void initialize() {
        CSC.LOGGER.info("Initializing Key Mappings");
        registerKeyInputs();
    }

    private static void registerKeyInputs() {
        registerTrinketActivationKey(faceActivationKey, "head/face", 0);
        registerTrinketActivationKey(capeActivationKey, "chest/cape", 0);
        registerTrinketActivationKey(beltActivationKey0, "legs/belt", 0);
        registerTrinketActivationKey(beltActivationKey1, "legs/belt", 1);
        registerTrinketActivationKey(agletActivationKey, "feet/aglet", 0);

        registerTrinketActivationKey(abilityActivationKey0, "offhand/ability", 0);
        registerTrinketActivationKey(abilityActivationKey1, "offhand/ability", 1);
    }

    private static KeyMapping register(String name, int key) {
        return register(name, key, null);
    }

    private static KeyMapping register(String name, int key, @Nullable Integer index) {
        String nameKey = "key.%s.%s_activation".formatted(CSC.MOD_ID, name);

        if (index != null) {
            nameKey = nameKey + "." + index;
        }

        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
                nameKey,
                InputConstants.Type.KEYBOARD,
                key,
                ACTIVATION_CATEGORY
        ));
    }

    private static void registerTrinketActivationKey(KeyMapping keyMapping, String slotGroup, int slotOffset) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyMapping.consumeClick()) {
                if (client.player == null) continue;

                ActivateTrinketPayload payload = new ActivateTrinketPayload(slotGroup, slotOffset);
                ClientPlayNetworking.send(payload);
            }
        });
    }
}
