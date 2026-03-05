package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.DarkGauntletAbility;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class DarkGauntlet extends BaseActiveItem {
    // Item Properties
    private static final int COOLDOWN = 1;
    private static final int DURATION = 0;

    private static final double BASE_DAMAGE = 2.0;
    private static final double ABILITY_DAMAGE = 7.0;
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
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", COOLDOWN).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("item.csc.dark_gauntlet.description.1", ABILITY_DAMAGE).withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("item.csc.dark_gauntlet.description.2", wrapDecimalAsPercent(INCOMING_DAMAGE), wrapDecimalAsPercent(TICKING_DAMAGE)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (isClientSide(player)) return InteractionResult.PASS;
        if (isOnCooldown(player, stack)) {
            player.level().playSound(null, player.blockPosition(), CSCSounds.ITEM_IN_COOLDOWN, SoundSource.PLAYERS, 1f, 1f);
            return InteractionResult.FAIL;
        };

        // Set Cooldown
        player.getCooldowns().addCooldown(stack, getSecondsToTicks(COOLDOWN));

        // Activate
        AbilityHandler handler = player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        handler.addAbility(new DarkGauntletAbility(player, hand, getSecondsToTicks(DURATION), ABILITY_DAMAGE, TICKING_DAMAGE));

        return InteractionResult.SUCCESS;
    }
}