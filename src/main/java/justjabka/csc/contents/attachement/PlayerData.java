package justjabka.csc.contents.attachement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PlayerData {

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("gold").forGetter(PlayerData::getGold)
            ).apply(instance, PlayerData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> PACKET_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    PlayerData::getGold,
                    PlayerData::new
            );

    private int gold;

    public PlayerData() {
        this(0);
    }

    public PlayerData(int gold) {
        this.gold = gold;
    }

    // Gold
    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void setGold(int amount) {
        this.gold = amount;
    }
}