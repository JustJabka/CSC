package justjabka.csc.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record AbilityData(int duration, int maxDuration, Identifier icon) {
    public static final Codec<AbilityData> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("duration").forGetter(AbilityData::duration),
                Codec.INT.fieldOf("max_duration").forGetter(AbilityData::maxDuration),
                Identifier.CODEC.fieldOf("icon").forGetter(AbilityData::icon)
        ).apply(builder, AbilityData::new);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AbilityData::duration,
            ByteBufCodecs.INT, AbilityData::maxDuration,
            Identifier.STREAM_CODEC, AbilityData::icon,
            AbilityData::new
    );
}
