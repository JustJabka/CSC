package justjabka.csc.contents.attachement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record PlayerData(
        int gold,
        Map<Identifier, Integer> abilities
) {

    // Default
    public static final PlayerData DEFAULT =
            new PlayerData(0, Map.of());

    // Codec
    public static final Codec<PlayerData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("gold").forGetter(PlayerData::gold),
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT).fieldOf("abilities").forGetter(PlayerData::abilities)
            ).apply(instance, PlayerData::new));

    // Packet Codec
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerData> PACKET_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, PlayerData::gold,
                    ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.INT), PlayerData::abilities,
                    PlayerData::new
            );

    // Gold
    public PlayerData addGold(int amount) {
        return new PlayerData(this.gold + amount, this.abilities);
    }

    public PlayerData setGold(int value) {
        return new PlayerData(value, this.abilities);
    }

    // Abilities
    public PlayerData updateAbility(Identifier key, int duration) {
        if (duration <= 0) return removeAbility(key);

        Map<Identifier, Integer> abilities = new HashMap<>(this.abilities);

        abilities.put(key, duration);

        return new PlayerData(this.gold, Map.copyOf(abilities));
    }

    public PlayerData removeAbility(Identifier key) {
        if (!this.abilities.containsKey(key)) return this;

        Map<Identifier, Integer> abilities = new HashMap<>(this.abilities);
        abilities.remove(key);

        return new PlayerData(this.gold, Map.copyOf(abilities));
    }
}