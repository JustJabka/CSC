package justjabka.csc.contents.attachement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlayerData(
        int gold
) {

    // Default
    public static final PlayerData DEFAULT =
            new PlayerData(0);

    // Codec
    public static final Codec<PlayerData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("gold").forGetter(PlayerData::gold)
            ).apply(instance, PlayerData::new));

    // Packet Codec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> PACKET_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,PlayerData::gold,
                    PlayerData::new
            );

    // Logic
    public PlayerData addGold(int amount) {
        return new PlayerData(this.gold + amount);
    }

    public PlayerData setGold(int value) {
        return new PlayerData(value);
    }
}