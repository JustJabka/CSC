package justjabka.csc.contents.character;

import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TitanCharacter extends Character {
    public TitanCharacter(Properties properties) {
        super(properties
                .overrideAbilities(
                        List.of(
                                CSCItems.REDOUBT
                        )
                )
                .overrideBaseAttributes(
                        Map.of(
                                Attributes.MAX_HEALTH, 40.0,
                                CSCAttributes.HEALTH_BOOK_BONUS, 2.0
                        )
                )
                .overrideAttributeModifiers(
                        Map.of(
                                Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(
                                        getModifierKey(properties.characterIdOrThrow().identifier()),
                                        -0.05,
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                )
                        )
                )
        );
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
    public void getShardDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("shard.csc.titan.description.1").withStyle(ChatFormatting.GRAY));
        textConsumer.accept(Component.translatable("shard.csc.titan.description.2", CSCItems.REDOUBT.getDefaultInstance().getItemName()).withStyle(ChatFormatting.GRAY));
    }
}
