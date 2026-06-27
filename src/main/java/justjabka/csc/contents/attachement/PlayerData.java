package justjabka.csc.contents.attachement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.registries.CSCCharacters;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record PlayerData(
        int gold,
        Identifier character
) {

    // Default
    public static final Identifier DEFAULT_CHARACTER = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "none");

    public static final PlayerData DEFAULT =
            new PlayerData(0, DEFAULT_CHARACTER);

    // Codec
    public static final Codec<PlayerData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("gold").forGetter(PlayerData::gold),
                    Identifier.CODEC.fieldOf("character").forGetter(PlayerData::character)
            ).apply(instance, PlayerData::new));

    // Packet Codec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> PACKET_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, PlayerData::gold,
                    Identifier.STREAM_CODEC, PlayerData::character,
                    PlayerData::new
            );

    // Gold
    public PlayerData addGold(int amount) {
        return new PlayerData(this.gold + amount, this.character);
    }

    public PlayerData removeGold(int amount) {
        return new PlayerData(this.gold - amount, this.character);
    }

    public PlayerData setGold(int value) {
        return new PlayerData(value, this.character);
    }

    // Character
    public PlayerData setCharacter(Identifier key) {
        return new PlayerData(this.gold, key);
    }

    public BaseCharacter getCharacter() {
        if (this.character.equals(DEFAULT_CHARACTER)) return null;
        return CSCCharacters.getByKey(this.character);
    }
}