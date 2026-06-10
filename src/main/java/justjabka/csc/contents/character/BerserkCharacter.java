package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
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

public class BerserkCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "berserk");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public void getShardDescription(@NonNull ItemStack stack, Item.TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.literal("test").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 18.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of();
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.BLOODY_LARYNX, 0
        );
    }
}
