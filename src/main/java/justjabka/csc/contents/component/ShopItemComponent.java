package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record ShopItemComponent(int price, ShopCategory category) implements TooltipProvider {
    public static final Codec<ShopItemComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("price").forGetter(ShopItemComponent::price),
                ShopCategory.CODEC.fieldOf("category").forGetter(ShopItemComponent::category)
        ).apply(builder, ShopItemComponent::new);
    });

    @Override
    public void addToTooltip(Item.TooltipContext tooltip, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("other.csc.price", this.price).withStyle(ChatFormatting.GOLD));
    }
}
