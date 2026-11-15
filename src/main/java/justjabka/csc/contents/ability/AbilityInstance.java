package justjabka.csc.contents.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class AbilityInstance {
    public static final Codec<AbilityInstance> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            Ability.CODEC.fieldOf("id").forGetter(AbilityInstance::getAbility), AbilityInstance.Details.MAP_CODEC.forGetter(AbilityInstance::asDetails)
                    )
                    .apply(i, AbilityInstance::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityInstance> STREAM_CODEC = StreamCodec.composite(
            Ability.STREAM_CODEC, AbilityInstance::getAbility, AbilityInstance.Details.STREAM_CODEC, AbilityInstance::asDetails, AbilityInstance::new
    );

    private final Holder<Ability> ability;
    private AbilityContext ctx;
    private int duration;
    private int maxDuration;
    private final Identifier icon;

    public AbilityInstance(final Holder<Ability> ability, final int duration, Identifier icon) {
        this.ability = ability;
        this.duration = duration;
        this.maxDuration = duration;
        this.icon = icon;
    }

    public AbilityInstance(final Holder<Ability> ability, final int duration, final int maxDuration, Identifier icon) {
        this.ability = ability;
        this.duration = duration;
        this.maxDuration = maxDuration;
        this.icon = icon;
    }

    private AbilityInstance(final Holder<Ability> ability, final AbilityInstance.Details details) {
        this(ability, details.duration(), details.maxDuration(), details.icon());
    }

    public AbilityInstance(AbilityInstance copy) {
        this.ability = copy.ability;
        this.duration = copy.duration;
        this.maxDuration = copy.maxDuration;
        this.icon = copy.icon;
        this.ctx = copy.ctx;
    }


    public Holder<Ability> getAbility() {
        return this.ability;
    }

    public AbilityContext getContext() {
        return this.ctx;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getMaxDuration() {
        return this.maxDuration;
    }

    public static Identifier createIcon(ItemStack item) {
        return BuiltInRegistries.ITEM.getKey(item.getItem());
    }

    public Identifier getIcon() {
        return this.icon;
    }


    public void start(AbilityContext ctx) {
        this.ctx = ctx;

        Ability type = this.ability.value();

        type.onStart(this.ctx);
        type.addAttributeModifiers(this.ctx.player.getAttributes());
    }

    public boolean tick() {
        Ability type = this.ability.value();

        boolean ended = ctx.player.isDeadOrDying() || !hasRemainingDuration() || type.shouldEnd(ctx);
        if (ended) return false;

        this.duration = type.tickDuration(this.duration);
        return type.onTick(ctx);
    }

    public void end() {
        Ability type = this.ability.value();

        type.onEnd(ctx);
        type.removeAttributeModifiers(ctx.player.getAttributes());
    }


    public boolean hasRemainingDuration() {
        if (this.ability.value() instanceof TogglableAbility) return true;
        return this.duration > 0;
    }

    public void update(AbilityInstance newAbility) {
        this.ctx = newAbility.getContext();
        this.duration = newAbility.getDuration();
        this.maxDuration = newAbility.getMaxDuration();
    }

    private AbilityInstance.Details asDetails() {
        return new AbilityInstance.Details(
                this.duration,
                this.maxDuration,
                this.icon
        );
    }

    private record Details(int duration, int maxDuration, Identifier icon) {

        public static final MapCodec<AbilityInstance.Details> MAP_CODEC = MapCodec.recursive(
                "AbilityInstance.Details",
                _ -> RecordCodecBuilder.mapCodec(
                        i -> i.group(
                                        Codec.INT.optionalFieldOf("duration", 0).forGetter(AbilityInstance.Details::duration),
                                        Codec.INT.optionalFieldOf("max_duration", 0).forGetter(AbilityInstance.Details::maxDuration),
                                        Identifier.CODEC.fieldOf("icon").forGetter(AbilityInstance.Details::icon)
                                )
                                .apply(i, AbilityInstance.Details::new)
                )
        );

        public static final StreamCodec<ByteBuf, AbilityInstance.Details> STREAM_CODEC = StreamCodec.recursive(
                _ -> StreamCodec.composite(
                        ByteBufCodecs.VAR_INT,
                        AbilityInstance.Details::duration,
                        ByteBufCodecs.VAR_INT,
                        AbilityInstance.Details::maxDuration,
                        Identifier.STREAM_CODEC,
                        AbilityInstance.Details::icon,
                        AbilityInstance.Details::new
                )
        );
    }
}
