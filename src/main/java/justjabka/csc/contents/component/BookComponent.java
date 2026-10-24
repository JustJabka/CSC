package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Consumer;

public record BookComponent(
        double amount,
        Holder<Attribute> attribute,
        Optional<Holder<Attribute>> bonusAttribute
) implements TooltipProvider {
    public static final Codec<BookComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.DOUBLE.fieldOf("amount").forGetter(BookComponent::amount),
                    Attribute.CODEC.fieldOf("attribute").forGetter(BookComponent::attribute),
                    Attribute.CODEC.optionalFieldOf("bonus_attribute").forGetter(BookComponent::bonusAttribute)
            ).apply(builder, BookComponent::new)
    );

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (player.level().isClientSide()) return InteractionResult.PASS;
        item.consume(1, player);

        applyAttribute(player);

        return InteractionResult.SUCCESS;
    }

    private void applyAttribute(Player player) {
        // Get Attribute Value
        AttributeInstance instance = player.getAttribute(this.attribute);
        if (instance == null) return;

        double value = instance.getBaseValue();

        // Apply Bonus
        instance.setBaseValue(value + calcBonusValue(player));

        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 1f);
    }

    private double calcBonusValue(Player player) {
        if (this.bonusAttribute.isEmpty()) return this.amount;

        AttributeInstance bookBonusInstance = player.getAttribute(this.bonusAttribute.get());
        double bookBonus = bookBonusInstance == null ? 0 : bookBonusInstance.getValue();

        return this.amount + bookBonus;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        Minecraft mc = Minecraft.getInstance();

        LocalPlayer player = mc.player;
        if (player == null) return;

        Component attributeName = Component.translatable(this.attribute.value().getDescriptionId()).withStyle(ChatFormatting.GREEN);

        consumer.accept(Component.translatable("other.csc.book.description",
                attributeName,
                calcBonusValue(player)
        ).withStyle(ChatFormatting.GRAY));
    }
}
