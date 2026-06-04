package justjabka.csc.contents.character.generic;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import justjabka.csc.CSC;
import justjabka.csc.handlers.AttributeHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.SequencedSet;

public abstract class BaseCharacter {
    private static final String ABILITY_SLOT_ID = "offhand/ability";

    // Getters
    public abstract Identifier getKey();
    public abstract Identifier getDisplayIcon();
    public Component getDisplayName() {
        String key = "character.%s".formatted(getKey());
        return Component.translatableWithFallback(key, "Sorry the translate broke :(");
    }

    public abstract Map<Holder<Attribute>, Double> getBaseAttributes();
    public abstract Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers();

    public abstract Map<Item, Integer> getAbilities();

    private void updateAbilities(Player player) {
        final Map<Item, Integer> abilities = getAbilities();

        TrinketAttachment trinketAttachment = TrinketsApi.getAttachment(player);

        clearAbilities(trinketAttachment);
        setAbilities(player, abilities, trinketAttachment);
    }

    private static void setAbilities(Player player, Map<Item, Integer> abilities, TrinketAttachment trinketAttachment) {
        abilities.forEach((ability, index) -> {
            TrinketSlotAccess slotAccess = trinketAttachment.getSlotAccess(ABILITY_SLOT_ID, index);

            if (slotAccess == null) return;
            if (!slotAccess.isValid()) return;

            ItemStack abilityStack = ability.getDefaultInstance();

            // Get shit
            HolderLookup.RegistryLookup<Enchantment> enchLookup = player.registryAccess().lookupOrThrow(Enchantments.BINDING_CURSE.registryKey());
            Holder<Enchantment> bindingCurse = enchLookup.getOrThrow(Enchantments.BINDING_CURSE);

            SequencedSet<DataComponentType<?>> hiddenComponents = new LinkedHashSet<>();
            hiddenComponents.add(DataComponents.ENCHANTMENTS);

            // Apply shit
            abilityStack.enchant(bindingCurse, 1);
            abilityStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            abilityStack.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, hiddenComponents));

            slotAccess.set(abilityStack);
        });
    }

    private static void clearAbilities(TrinketAttachment trinketAttachment) {
        TrinketInventory trinketInventory = trinketAttachment.getInventory(ABILITY_SLOT_ID);
        if (trinketInventory == null) return;

        trinketInventory.clearContent();
    }

    // Attributes
    private void setBaseAttributes(Player player) {
        AttributeHandler.resetBaseValues(player);
        AttributeHandler.setBaseValues(player, getBaseAttributes());
    }

    private void applyAttributeModifiers(Player player) {
        Map<Holder<Attribute>, AttributeModifier> modifiers = getAttributeModifiers();

        AttributeHandler.removeAllModifiersFromNamespace(player, CSC.MOD_ID);
        AttributeHandler.addTransientModifiers(player, modifiers);
    }

    // Other
    public void onSelect(Player player) {
        setBaseAttributes(player);
        applyAttributeModifiers(player);

        player.heal(player.getMaxHealth());

        updateAbilities(player);
    }
}
