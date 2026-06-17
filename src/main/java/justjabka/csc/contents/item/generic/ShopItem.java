package justjabka.csc.contents.item.generic;

import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.types.ShopCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface ShopItem {
    int getPrice();
    ShopCategory getCategory();

    @Environment(EnvType.CLIENT)
    default void getPriceDescription(
            @NonNull ItemStack stack,
            Item.@NonNull TooltipContext context,
            @NonNull TooltipDisplay displayComponent,
            Consumer<Component> textConsumer,
            @NonNull TooltipFlag type
    ) {
        if (!isInShop()) return;

        textConsumer.accept(Component.translatable("other.csc.price",getPrice())
                .withStyle(ChatFormatting.GOLD)
                .withStyle(ChatFormatting.UNDERLINE)
        );
    }

    @Environment(EnvType.CLIENT)
    private boolean isInShop() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return false;

        return player.containerMenu instanceof ShopMenu;
    }
}
