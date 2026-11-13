package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.types.ShopCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record ShopItemComponent(int price, ShopCategory category, int waveReq) implements TooltipProvider {
    public static final Codec<ShopItemComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("price").forGetter(ShopItemComponent::price),
            ShopCategory.CODEC.fieldOf("category").forGetter(ShopItemComponent::category),
            Codec.INT.fieldOf("wave_req").forGetter(ShopItemComponent::waveReq)
    ).apply(builder, ShopItemComponent::new));

    public ShopItemComponent(int price, ShopCategory category) {
        this(price, category, 0);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void addToTooltip(
            Item.@NonNull TooltipContext tooltip,
            @NonNull Consumer<Component> textConsumer,
            @NonNull TooltipFlag type,
            @NonNull DataComponentGetter components
    ) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;
        if (!(player.containerMenu instanceof ShopMenu)) return;

        textConsumer.accept(Component.translatable("other.csc.shop_item.price", this.price).withStyle(ChatFormatting.GOLD));
        if (this.waveReq > 0) {
            textConsumer.accept(Component.translatable("other.csc.shop_item.wave_req", this.waveReq).withStyle(ChatFormatting.RED));
        }
    }
}
