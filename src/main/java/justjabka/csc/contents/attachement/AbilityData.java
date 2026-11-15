package justjabka.csc.contents.attachement;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.contents.ability.AbilityInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record AbilityData(Map<Holder<Ability>, AbilityInstance> abilities) {

    public static final AbilityData DEFAULT = new AbilityData(Collections.emptyMap());

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, Ability.STREAM_CODEC, AbilityInstance.STREAM_CODEC),
            AbilityData::abilities,
            AbilityData::new
    );

    public AbilityData addAbility(AbilityInstance ability) {
        Map<Holder<Ability>, AbilityInstance> newAbilities = new HashMap<>(this.abilities);
        newAbilities.put(ability.getAbility(), ability);

        return new AbilityData(Map.copyOf(newAbilities));
    }

    public AbilityData removeAbility(AbilityInstance ability) {
        return removeAbility(ability.getAbility());
    }

    public AbilityData removeAbility(Holder<Ability> type) {
        Map<Holder<Ability>, AbilityInstance> newAbilities = new HashMap<>(this.abilities);
        newAbilities.remove(type);

        return new AbilityData(Map.copyOf(newAbilities));
    }
}
