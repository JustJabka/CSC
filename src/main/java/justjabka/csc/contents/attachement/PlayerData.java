package justjabka.csc.contents.attachement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.csc.contents.character.Character;
import justjabka.csc.registries.CSCBuiltInRegistries;
import justjabka.csc.registries.CSCCharacters;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlayerData(
        int gold,
        Holder<Character> character
) {

    // Default
    public static final PlayerData DEFAULT =
            new PlayerData(0, CSCBuiltInRegistries.CHARACTER.wrapAsHolder(CSCCharacters.NONE));

    // Codec
    public static final Codec<PlayerData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("gold").forGetter(PlayerData::gold),
                    Character.CODEC.fieldOf("character").forGetter(PlayerData::character)
            ).apply(instance, PlayerData::new));

    // Packet Codec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, PlayerData::gold,
                    Character.STREAM_CODEC, PlayerData::character,
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
    public Character getCharacter() {
        return this.character.value();
    }

    public PlayerData setCharacter(Character character) {
        return new PlayerData(this.gold, CSCBuiltInRegistries.CHARACTER.wrapAsHolder(character));
    }
}