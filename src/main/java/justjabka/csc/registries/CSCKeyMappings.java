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
    public static KeyMapping beltActivationKey;
    public static KeyMapping agletActivationKey;

    public static void initialize() {
        CSC.LOGGER.info("Initializing Key Mappings");
        register();
        registerKeyInputs();
    }

    private static void registerKeyInputs() {
        registerTrinketActivationKey(faceActivationKey, "head/face");
        registerTrinketActivationKey(capeActivationKey, "chest/cape");
        registerTrinketActivationKey(beltActivationKey, "legs/belt");
        registerTrinketActivationKey(agletActivationKey, "feet/aglet");
    }

    private static void register() {
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
        beltActivationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.belt_activation",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                ACTIVATION_CATEGORY
        ));
        agletActivationKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.csc.aglet_activation",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                ACTIVATION_CATEGORY
        ));
    }

    private static void registerTrinketActivationKey(KeyMapping keyMapping, String slotGroup) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyMapping.consumeClick()) {
                if (client.player == null) continue;

                ActivateTrinketPayload payload = new ActivateTrinketPayload(slotGroup);
                ClientPlayNetworking.send(payload);
            }
        });
    }
}
