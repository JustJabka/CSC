package justjabka.csc.contents.item.books;

import justjabka.csc.contents.item.generic.BaseBook;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class HealthBook extends BaseBook {
    public HealthBook(Properties properties) {
        super(properties);
    }

    @Override
    protected double getBonusValue() {
        return 4;
    }

    @Override
    protected Holder<Attribute> getBonusAttribute() {
        return Attributes.MAX_HEALTH;
    }

    @Override
    protected Holder<Attribute> getBookBonusAttribute() {
        return CSCAttributes.HEALTH_BOOK_BONUS;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        textConsumer.accept(Component.translatable("item.csc.health_book.description",
                MAX_HEALTH,
                calcBonusValue(player)
        ).withStyle(ChatFormatting.GRAY));
    }
}