package justjabka.csc.contents.character;

import justjabka.csc.contents.ability.Ability;
import justjabka.csc.contents.item.ability.SpinningSwords;
import justjabka.csc.registries.CSCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static justjabka.csc.handlers.DescriptionHandler.MAGICAL_DAMAGE;
import static justjabka.csc.handlers.DescriptionHandler.wrapDecimalAsPercent;

public class SwordsmanCharacter extends Character {

    public SwordsmanCharacter(Properties properties) {
        super(properties
                .overrideAbilities(
                        List.of(
                                CSCItems.SPINNING_SWORDS
                        )
                )
                .overrideBaseAttributes(
                        Map.of(
                                Attributes.MAX_HEALTH, 22.0,
                                Attributes.ATTACK_DAMAGE, 1.0
                        )
                )
                .overrideAttributeModifiers(
                        Map.of(
                                Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(
                                        getModifierKey(properties.characterIdOrThrow().identifier()),
                                        0.16,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                                )
                        )
                )
        );
    }

    @Override
    public Holder<Ability> getShardAbility() {
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
    public void getShardDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("shard.csc.swordsman.description",
                MAGICAL_DAMAGE,
                CSCItems.SPINNING_SWORDS.getDefaultInstance().getItemName(),
                wrapDecimalAsPercent(SpinningSwords.DAMAGE_PERCENT),
                wrapDecimalAsPercent(SpinningSwords.DAMAGE_PERCENT + SpinningSwords.DAMAGE_PERCENT_SHARD_BONUS)
        ).withStyle(ChatFormatting.GRAY));
    }
}
