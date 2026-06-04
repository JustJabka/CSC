package justjabka.csc.registries;

import com.mojang.blaze3d.platform.InputConstants;
import justjabka.csc.CSC;
import justjabka.csc.payloads.ActivateTrinketPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class CSCKeyMappings {
    public static final KeyMapping.Category ACTIVATION_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(CSC.MOD_ID, "activation")
    );

    public static KeyMapping faceActivationKey;
    public static KeyMapping capeActivationKey;
    public static KeyMapping beltActivationKey0;
    public static KeyMapping beltActivationKey1;
    public static KeyMapping agletActivationKey;

    public static KeyMapping abilityActivationKey0;
    public static KeyMapping abilityActivationKey1;

    public static void initialize() {
        CSC.LOGGER.info("Initializing Key Mappings");
        register();
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

    private static void register() {
        // Trinkets
        faceActivationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.face_activation",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                ACTIVATION_CATEGORY
        ));
        capeActivationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.cape_activation",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                ACTIVATION_CATEGORY
        ));
        beltActivationKey0 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.belt_activation.0",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                ACTIVATION_CATEGORY
        ));
        beltActivationKey1 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.belt_activation.1",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                ACTIVATION_CATEGORY
        ));
        agletActivationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.aglet_activation",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                ACTIVATION_CATEGORY
        ));

        // Abilities
        abilityActivationKey0 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.ablility_activation.0",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                ACTIVATION_CATEGORY
        ));
        abilityActivationKey1 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.ablility_activation.1",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
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
