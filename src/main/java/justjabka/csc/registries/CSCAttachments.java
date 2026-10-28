package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.AbilitiesData;
import justjabka.csc.contents.attachement.PlayerData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class CSCAttachments {
    public static final AttachmentType<PlayerData> PLAYER_DATA = AttachmentRegistry.create(
            CSC.id("player_data"),
            builder -> builder
                    .initializer(() -> PlayerData.DEFAULT)
                    .persistent(PlayerData.CODEC)
                    .syncWith(PlayerData.PACKET_CODEC, AttachmentSyncPredicate.targetOnly())
                    .copyOnDeath()
    );

    public static final AttachmentType<AbilitiesData> ABILITIES_DATA = AttachmentRegistry.create(
            CSC.id("abilities_data"),
            builder -> builder
                    .initializer(() -> AbilitiesData.DEFAULT)
                    .syncWith(AbilitiesData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
    );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Attachments");
    }
}