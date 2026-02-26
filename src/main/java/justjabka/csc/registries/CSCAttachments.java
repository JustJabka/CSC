package justjabka.csc.registries;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public class CSCAttachments {
    public static final AttachmentType<PlayerData> PLAYER_DATA =
            AttachmentRegistry.create(
                    Identifier.fromNamespaceAndPath("csc", "player_data"),
                    builder -> builder
                            .initializer(PlayerData::new)
                            .persistent(PlayerData.CODEC)
                            .syncWith(PlayerData.PACKET_CODEC, AttachmentSyncPredicate.all())
            );

    public static void initialize() {
        CSC.LOGGER.info("Initializing Attachments");
    }
}