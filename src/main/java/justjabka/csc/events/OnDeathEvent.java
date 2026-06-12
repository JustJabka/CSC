package justjabka.csc.events;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.handlers.TrinketHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCItems;
import justjabka.csc.types.ShardContext;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

import static justjabka.csc.registries.CSCCharacters.BERSERK;

public class OnDeathEvent {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
            if (!(entity instanceof Player player)) return true;

            return handleBerserkShard(player);
        });
    }

    private static boolean handleBerserkShard(Player player) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        BaseCharacter character = data.getCharacter();

        if (character != BERSERK) return true;

        ItemStack stack = TrinketHandler.findFirstTrinket(player, CSCItems.SHARD, "legs/belt");
        if (stack.isEmpty()) return true;

        ItemCooldowns cooldowns = player.getCooldowns();
        boolean isOnCooldown = cooldowns.isOnCooldown(stack);

        if (isOnCooldown) return true;
        cooldowns.addCooldown(stack, character.getShardCooldown() * 20);

        ShardContext ctx = new ShardContext(player, stack, character);
        character.onShardTrigger(ctx);

        return false;
    }
}
