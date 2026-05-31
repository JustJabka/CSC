package justjabka.csc.registries;

import com.mojang.blaze3d.platform.InputConstants;
import justjabka.csc.CSC;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
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
    }

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (capeActivationKey.consumeClick()) {
                if (client.player == null) continue;

                client.player.sendSystemMessage(Component.literal("Test"));
            }
        });
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

        registerKeyInputs();
    }
}
