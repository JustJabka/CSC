package justjabka.csc.contents.item.book;

import justjabka.csc.contents.item.generic.BaseBook;
import justjabka.csc.registries.CSCAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

public class DamageBook extends BaseBook {
    public DamageBook(Properties properties) {
        super(properties);
    }

    @Override
    protected double getBonusValue() {
        return 1;
    }

    @Override
    protected Holder<Attribute> getBonusAttribute() {
        return Attributes.ATTACK_DAMAGE;
    }

    @Override
    protected Holder<Attribute> getBookBonusAttribute() {
        return CSCAttributes.DAMAGE_BOOK_BONUS;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        textConsumer.accept(Component.translatable("item.csc.damage_book.description", PHYSICAL_DAMAGE, calcBonusValue(player)).withStyle(ChatFormatting.GRAY));
    }
}