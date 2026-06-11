package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.CharacterHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Shard extends BaseActiveTrinketItem {
    public Shard(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "shard");
    }

    @Override
    protected int getCooldown() {
        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return 0;

        return character.getShardCooldown();
    }

    @Override
    protected int getDuration() {
        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return 0;

        return character.getShardDuration();
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return null;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);

        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return;

        character.getShardDescription(stack, context, displayComponent, textConsumer, type);
    }
}
