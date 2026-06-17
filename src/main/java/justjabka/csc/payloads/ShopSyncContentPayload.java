package justjabka.csc.payloads;

import justjabka.csc.CSC;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ShopSyncContentPayload(String query) implements CustomPacketPayload {
    public static final Type<ShopSyncContentPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(CSC.MOD_ID, "shop_sync_content"));

    public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ShopSyncContentPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ShopSyncContentPayload::query,
            ShopSyncContentPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}