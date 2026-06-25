package justjabka.csc.contents.character;

import justjabka.csc.CSC;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.item.ability.SpinningSwords;
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

import static justjabka.csc.handlers.DescriptionHandler.MAGICAL_DAMAGE;
import static justjabka.csc.handlers.DescriptionHandler.wrapDecimalAsPercent;

public class SwordsmanCharacter extends BaseCharacter {
    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "swordsman");
    }

    @Override
    public Identifier getDisplayIcon() {
        return null;
    }

    @Override
    public Identifier getShardAbility() {
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
        textConsumer.accept(Component.translatable("shard.csc.swordsman.description",
                MAGICAL_DAMAGE,
                CSCItems.SPINNING_SWORDS.getDefaultInstance().getItemName(),
                wrapDecimalAsPercent(SpinningSwords.DAMAGE_PERCENT),
                wrapDecimalAsPercent(SpinningSwords.DAMAGE_PERCENT + SpinningSwords.DAMAGE_PERCENT_SHARD_BONUS)
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Map<Holder<Attribute>, Double> getBaseAttributes() {
        return Map.of(
                Attributes.MAX_HEALTH, 22.0,
                Attributes.ATTACK_DAMAGE, 1.0
        );
    }

    @Override
    public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of(
                Attributes.ATTACK_DAMAGE, new AttributeModifier(getKey(), 0.16, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        );
    }

    @Override
    public Map<Item, Integer> getAbilities() {
        return Map.of(
                CSCItems.SPINNING_SWORDS, 0
        );
    }
}
