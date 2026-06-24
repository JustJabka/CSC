package justjabka.csc.events;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.item.HolyBlanketAbility;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.handlers.AbilityHandler;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.AbilityType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HolyBlanketDeathProtectionEvent {
    public static void register() {
        Identifier eventKey = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "holy_blanket_protection");

        ServerLivingEntityEvents.ALLOW_DEATH.register(eventKey, (entity, damageSource, damageAmount) -> {
            if (!(entity instanceof Player player)) return true;
            return handleDeathProtection(player, damageAmount);
        });

        ServerLivingEntityEvents.ALLOW_DEATH.addPhaseOrdering(eventKey, Event.DEFAULT_PHASE);
    }

    public static boolean handleDeathProtection(Player player, float damageAmount) {
        ItemStack stack = TrinketHandler.findFirstTrinket(player, CSCItems.HOLY_BLANKET, "chest/cape");
        if (stack.isEmpty()) return true;

        AbilityComponent ability = stack.get(CSCComponents.ABILITY);
        if (ability == null) return true;

        AbilityHandler handler = player.getAttachedOrCreate(CSCAttachments.ABILITY_HANDLER);
        HolyBlanketAbility blanketAbility = handler.getAbility(HolyBlanketAbility.class);

        boolean isValidState = blanketAbility != null && blanketAbility.getState() == HolyBlanketAbility.State.PARRYING;

        if (isValidState) {
            blanketAbility.activateStrongProtection(damageAmount);
            return false;
        }

        return handleWeakDeathProtection(player, ability, stack);
    }

    private static boolean handleWeakDeathProtection(Player player, AbilityComponent ability, ItemStack stack) {
        if (ability.isOnCooldown(player, stack)) return true;
        ability.applyCooldown(player, stack);

        AbilityType<?> abilityType = CSCAbilities.getByKey(ability.id());
        if (abilityType == null) return true;

        AbilityContext ctx = new AbilityContext(player, stack);
        HolyBlanketAbility holyBlanketAbility = (HolyBlanketAbility) abilityType.createInstance(ability.duration(), ctx);
        holyBlanketAbility.activateWeakProtection();

        return false;
    }
}
