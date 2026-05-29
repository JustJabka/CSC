package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.DarkGauntletAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.ActiveItemConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class DarkGauntlet extends BaseActiveItem {
    private static final double BASE_DAMAGE = 2.0;
    private static final double ABILITY_DAMAGE = 7.0;
    private static final double ABILITY_INCOMING_DAMAGE = 0.02;
    private static final double INCOMING_DAMAGE = 0.02;
    private static final double TICKING_DAMAGE = 0.01;

    public DarkGauntlet(Properties properties) {
        super(properties
                .rarity(Rarity.EPIC)
                .attributes(ItemAttributeModifiers.builder()
                    .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                    BASE_ATTACK_DAMAGE_ID,
                                    BASE_DAMAGE,
                                    AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.HAND
                    )
                    .build()
                ),
                new ActiveItemConfig(
                        true,
                        true,
                        1,
                        0
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("item.csc.dark_gauntlet.description.1", ABILITY_DAMAGE).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.dark_gauntlet.description.2", wrapDecimalAsPercent(INCOMING_DAMAGE), wrapDecimalAsPercent(TICKING_DAMAGE)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected BaseActiveAbility createAbility() {
        return new DarkGauntletAbility(getSecondsToTicks(config.duration), ABILITY_DAMAGE, ABILITY_INCOMING_DAMAGE, TICKING_DAMAGE);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        return InteractionResult.PASS;
    }
}