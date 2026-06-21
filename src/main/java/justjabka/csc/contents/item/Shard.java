package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.CharacterHandler;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ShopCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Shard extends BaseActiveTrinketItem {
    public Shard(Properties properties) {
        super(properties.component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2300, ShopCategory.MAGIC)));
    }

    @Override
    public Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "shard");
    }

    @Override
    public int getCooldown() {
        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return 0;

        return character.getShardCooldown();
    }

    @Override
    public int getDuration() {
        BaseCharacter character = CharacterHandler.getClientCharacter();
        if (character == null) return 0;

        return character.getShardDuration();
    }

    @Override
    public BaseActiveAbility getAbility() {
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
