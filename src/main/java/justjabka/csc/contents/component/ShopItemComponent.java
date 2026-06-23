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

import java.util.function.Consumer;

public record ShopItemComponent(int price, ShopCategory category) implements TooltipProvider {
    public static final Codec<ShopItemComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("price").forGetter(ShopItemComponent::price),
                ShopCategory.CODEC.fieldOf("category").forGetter(ShopItemComponent::category)
        ).apply(builder, ShopItemComponent::new);
    });

    @Override
    @Environment(EnvType.CLIENT)
    public void addToTooltip(Item.TooltipContext tooltip, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;
        if (!(player.containerMenu instanceof ShopMenu)) return;

        textConsumer.accept(Component.translatable("other.csc.price", this.price).withStyle(ChatFormatting.GOLD));
    }
}
