package justjabka.csc.events;

import justjabka.csc.CSC;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.events.callback.OnPlayerHealthChangeCallback;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import static justjabka.csc.registries.CSCCharacters.BERSERK;

public class OnPlayerHealthChangeEvent {
    private static final Identifier identifier = Identifier.fromNamespaceAndPath(CSC.MOD_ID, "test");

    public static void register() {
        OnPlayerHealthChangeCallback.EVENT.register((player, oldHealth, newHealth) -> {
            handleBerserkPassiveAbility(player, newHealth);
        });
    }

    private static void handleBerserkPassiveAbility(Player player, float health) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        BaseCharacter character = data.getCharacter();

        if (character != BERSERK) return;

        float maxHealth = player.getMaxHealth();
        float lostHealthPercent = (maxHealth - health) / maxHealth;

        int step = (int) (lostHealthPercent * 10);

        double damageBonus = step * 0.03;
        double attackSpeedBonus = step * 0.02;

        updateBerserkPassiveAbility(player, Attributes.ATTACK_DAMAGE, damageBonus);
        updateBerserkPassiveAbility(player, Attributes.ATTACK_SPEED, attackSpeedBonus);
    }

    private static void updateBerserkPassiveAbility(Player player, Holder<Attribute> attribute, double value) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        AttributeModifier modifier = instance.getModifier(identifier);
        if (modifier == null || modifier.amount() != value) {
            if (modifier != null) {
                instance.removeModifier(identifier);
            }

            if (value < 0) return;
            AttributeModifier newModifier = new AttributeModifier(
                    identifier,
                    value,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
            instance.addTransientModifier(newModifier);
        }
    }
}
