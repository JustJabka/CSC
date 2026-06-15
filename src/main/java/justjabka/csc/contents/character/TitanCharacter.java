package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.function.Consumer;

public class TitanCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "titan");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public int getShardCooldown() {
        return 0;
    }

    @Override
    public int getShardDuration() {
        return 0;
    }

    @Override
    public void getShardDescription(@NonNull ItemStack stack, Item.TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("shard.csc.titan.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("shard.csc.titan.description.2", CSCItems.REDOUBT.getDefaultInstance().getItemName()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 40.0,
                CSCAttributes.HEALTH_BOOK_BONUS, 2.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of(
                Attributes.MOVEMENT_SPEED, new AttributeModifier(getKey(), -0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        );
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.REDOUBT, 0
        );
    }
}
