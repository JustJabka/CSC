package justjabka.csc.events;

import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OnItemUseEvent {
    public static void register() {
        ItemEvents.USE.register(OnItemUseEvent::useItemWithAbilityComponent);

    }

    private static InteractionResult useItemWithAbilityComponent(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        AbilityComponent abilityComponent = item.get(CSCComponents.ABILITY);
        if (abilityComponent == null) return InteractionResult.PASS;

        if (!abilityComponent.activationTypes().contains(ActivationType.GENERIC)) return InteractionResult.PASS;
        return abilityComponent.onUse(level, player, hand);
    }
}
