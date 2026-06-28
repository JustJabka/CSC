package justjabka.csc.contents.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.handlers.CharacterHandler;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.component.UseCooldown;

import java.util.function.Consumer;

public record ShardComponent() implements TooltipProvider {
    public static final ShardComponent INSTANCE = new ShardComponent();
    public static final Codec<ShardComponent> CODEC = MapCodec.unitCodec(INSTANCE);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return;

        UseCooldown useCooldown = components.get(DataComponents.USE_COOLDOWN);
        AbilityComponent ability = components.get(CSCComponents.ABILITY);

        int cooldown = useCooldown == null ? character.getShardCooldown() : useCooldown.ticks();
        int duration = ability == null || ability.duration() == 0 ? character.getShardDuration() : ability.duration();

        if (cooldown > 0) {
            textConsumer.accept(Component.translatable("other.csc.cooldown", TimeHandler.autoConvertTicks(cooldown)).withStyle(ChatFormatting.YELLOW));
        }

        if (duration > TimeHandler.secondsToTicks(1)) {
            textConsumer.accept(Component.translatable("other.csc.duration", TimeHandler.autoConvertTicks(duration)).withStyle(ChatFormatting.GREEN));
        }

        character.getShardDescription(context, textConsumer, type, components);
    }
}
