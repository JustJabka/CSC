package justjabka.csc.contents.ability.shard;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.types.AbilityContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class BerserkShardAbility extends BaseActiveAbility {
    private static final Holder<Attribute> activeAttribute = CSCAttributes.INCOMING_DAMAGE_MULTIPLIER;
    private final AttributeModifier activeModifier = new AttributeModifier(
            getId(),
            -1024,
            AttributeModifier.Operation.ADD_VALUE
    );

    public BerserkShardAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {}

    @Override
    public void onStart() {
        AttributeHandler.addTransientModifier(ctx.player, activeAttribute, activeModifier);
    }

    @Override
    public void onTick() {}

    @Override
    public void onEnd() {
        AttributeHandler.removeModifier(ctx.player, activeAttribute, activeModifier);
    }
}
