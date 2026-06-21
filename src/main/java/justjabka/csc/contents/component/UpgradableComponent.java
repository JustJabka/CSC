package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public record UpgradableComponent(
        int price,
        int priceScale,
        int maxLevel,
        Optional<Holder<Enchantment>> additionalEnchantment,
        int additionalDurability
) {
    public static final Codec<UpgradableComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("price").forGetter(UpgradableComponent::price),
                Codec.INT.optionalFieldOf("price_scale", 0).forGetter(UpgradableComponent::priceScale),
                Codec.INT.fieldOf("max_level").forGetter(UpgradableComponent::maxLevel),
                Enchantment.CODEC.optionalFieldOf("additional_enchantment").forGetter(UpgradableComponent::additionalEnchantment),
                Codec.INT.optionalFieldOf("additional_durability", 0).forGetter(UpgradableComponent::additionalDurability)
        ).apply(builder, UpgradableComponent::new);
    });
}
