package justjabka.csc.contents.attachement;

import justjabka.csc.types.AbilityData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record AbilitiesData(Map<Identifier, AbilityData> abilities) {

    public static final AbilitiesData DEFAULT = new AbilitiesData(Map.of());

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilitiesData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, AbilityData.STREAM_CODEC), AbilitiesData::abilities,
            AbilitiesData::new
    );

    public AbilitiesData updateAbility(Identifier key, AbilityData data) {
        if (data.duration() <= 0) return removeAbility(key);

        Map<Identifier, AbilityData> abilities = new HashMap<>(this.abilities);
        abilities.put(key, data);

        return new AbilitiesData(Map.copyOf(abilities));
    }

    public AbilitiesData removeAbility(Identifier key) {
        if (!this.abilities.containsKey(key)) return this;

        Map<Identifier, AbilityData> abilities = new HashMap<>(this.abilities);
        abilities.remove(key);

        return new AbilitiesData(Map.copyOf(abilities));
    }
}
