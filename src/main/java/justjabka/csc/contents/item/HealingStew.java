package justjabka.csc.contents.item;

import justjabka.csc.contents.item.generic.BaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class HealingStew extends BaseItem {
    private static final double HEAL_PERCENT = 0.1;

    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
            .nutrition(0)
            .saturationModifier(0)
            .alwaysEdible()
            .build();
    private static final Consumable CONSUMABLE = Consumable.builder()
            .animation(ItemUseAnimation.EAT)
            .consumeSeconds(0.6f)
            .hasConsumeParticles(true)
            .build();

    public HealingStew(Properties properties) {
        super(properties
                .stacksTo(8)
                .food(FOOD_PROPERTIES, CONSUMABLE)
        );
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getHealth() >= player.getMaxHealth()) {
            return InteractionResult.FAIL;
        }

        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        float maxHealth = entity.getMaxHealth();
        float healthToHeal = (float) (maxHealth * HEAL_PERCENT);

        entity.heal(healthToHeal);

        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag tooltipFlag) {
        textConsumer.accept(Component.translatable("item.csc.healing_stew.description", wrapDecimalAsPercent(HEAL_PERCENT)).withStyle(ChatFormatting.GRAY));
    }
}
