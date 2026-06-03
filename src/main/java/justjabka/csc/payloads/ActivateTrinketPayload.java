package justjabka.csc.payloads;

import justjabka.csc.CSC;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record ActivateTrinketPayload(String slotGroup, int slotOffset) implements CustomPacketPayload {
    public static final Identifier ACTIVATE_TRINKET_PAYLOAD_ID = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "activate_trinket");
    public static final CustomPacketPayload.Type<ActivateTrinketPayload> TYPE = new CustomPacketPayload.Type<>(ACTIVATE_TRINKET_PAYLOAD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ActivateTrinketPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ActivateTrinketPayload::slotGroup,
            ByteBufCodecs.INT, ActivateTrinketPayload::slotOffset,
            ActivateTrinketPayload::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}