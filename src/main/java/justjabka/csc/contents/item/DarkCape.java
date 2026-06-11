package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.CSC;
import justjabka.csc.contents.ability.item.DarkCapeAbility;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DarkCape extends BaseActiveTrinketItem {
    private static final double BASE_HEALTH = 4;
    private static final double BASE_DAMAGE = 1;

    private final double DAMAGE_MULTIPLIER = 2;
    private static final double VULNERABILITY_MODIFIER = 0.02;
    private static final double SPEED_MODIFIER = 0.15;

    private final Map<Holder<Attribute>, AttributeModifier> ACTIVE_MODIFIERS = Map.of(
            CSCAttributes.INCOMING_DAMAGE_MULTIPLIER, new AttributeModifier(
                    getKey(),
                    VULNERABILITY_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
            ),
            Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    getKey(),
                    SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )
    );

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "dark_cape");
    }

    @Override
    protected int getCooldown() {
        return 42;
    }

    @Override
    protected int getDuration() {
        return 12;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return new DarkCapeAbility(
                getKey(),
                getSecondsToTicks(getDuration()),
                DAMAGE_MULTIPLIER,
                ACTIVE_MODIFIERS
        );
    }

    public DarkCape(Properties properties) {
        super(properties.rarity(Rarity.EPIC));
    }

    @Override
    public void forEachTrinketModifier(
            ItemStack stack,
            TrinketSlotAccess slot,
            LivingEntity entity,
            Identifier key,
            BiConsumer<Holder<Attribute>, AttributeModifier> consumer
    ) {
        AttributeModifier healthModifier = new AttributeModifier(
                key.withSuffix("%s/max_health".formatted(CSC.MOD_ID)),
                BASE_HEALTH,
                AttributeModifier.Operation.ADD_VALUE
        );
        AttributeModifier damageModifier = new AttributeModifier(
                key.withSuffix("%s/attack_damage".formatted(CSC.MOD_ID)),
                BASE_DAMAGE,
                AttributeModifier.Operation.ADD_VALUE
        );

        consumer.accept(Attributes.MAX_HEALTH, healthModifier);
        consumer.accept(Attributes.ATTACK_DAMAGE, damageModifier);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.1",
                    wrapDecimalAsPercent(SPEED_MODIFIER),
                    wrapDecimalAsPercent(VULNERABILITY_MODIFIER)
                ).withStyle(ChatFormatting.GRAY)
        );
        textConsumer.accept(
                Component.translatable("item.csc.dark_cape.description.2",
                    DAMAGE_MULTIPLIER,
                    PHYSICAL_DAMAGE
                ).withStyle(ChatFormatting.GRAY)
        );
    }
}
