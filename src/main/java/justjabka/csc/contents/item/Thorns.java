package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.ThornsAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Thorns extends BaseActiveTrinketItem {
    private static final double DAMAGE_REFLECTION = 0.6;

    @Override
    protected int getCooldown() {
        return 45;
    }

    @Override
    protected int getDuration() {
        return 5;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new ThornsAbility(getSecondsToTicks(getDuration()), DAMAGE_REFLECTION);
    }

    public Thorns(Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Component.translatable("item.csc.thorns.description", wrapDecimalAsPercent(DAMAGE_REFLECTION)).withStyle(ChatFormatting.GRAY));
    }
}