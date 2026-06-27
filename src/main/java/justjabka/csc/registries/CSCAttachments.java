package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.AbilitiesData;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.handlers.AbilityHandler;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public class CSCAttachments {
    public static final AttachmentType<PlayerData> PLAYER_DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(CSC.MOD_ID, "player_data"),
            builder -> builder
                    .initializer(() -> PlayerData.DEFAULT)
                    .persistent(PlayerData.CODEC)
                    .syncWith(PlayerData.PACKET_CODEC, AttachmentSyncPredicate.targetOnly())
                    .copyOnDeath()
    );

    public static final AttachmentType<AbilitiesData> ABILITIES_DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(CSC.MOD_ID, "abilities_data"),
            builder -> builder
                    .initializer(() -> AbilitiesData.DEFAULT)
                    .syncWith(AbilitiesData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
    );

    public static final AttachmentType<AbilityHandler> ABILITY_HANDLER = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(CSC.MOD_ID, "ability_handler"),
            builder -> builder.initializer(AbilityHandler::new)
    );


    public static void initialize() {
        CSC.LOGGER.info("Initializing Attachments");
    }
}