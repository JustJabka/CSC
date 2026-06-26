package justjabka.csc.contents.item;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.component.ShardComponent;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAbilities;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.types.ActivationType;
import justjabka.csc.types.ShopCategory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class Shard extends BaseActiveTrinketItem {
    private static final AbilityComponent DEFAULT_SHARD_DATA = new AbilityComponent(CSCAbilities.EMPTY_ABILITY_ID, 0, Set.of(ActivationType.PASSIVE));

    public Shard(Properties properties) {
        super(properties.component(CSCComponents.ABILITY, DEFAULT_SHARD_DATA)
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(2300, ShopCategory.MAGIC))
                .component(CSCComponents.SHARD, ShardComponent.INSTANCE)
        );
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        tryUpdateShardData(itemStack, owner);
    }

    @Override
    public void tick(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        tryUpdateShardData(stack, entity);
    }

    private void updateShardData(Player player, ItemStack item) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        BaseCharacter character = data.getCharacter();
        if (character == null) {
            item.set(CSCComponents.ABILITY, DEFAULT_SHARD_DATA);
            return;
        }

        Identifier ability = character.getShardAbility();
        int cooldown = character.getShardCooldown();
        int duration = character.getShardDuration();

        item.set(CSCComponents.ABILITY, new AbilityComponent(ability, duration, Set.of(ActivationType.PASSIVE)));
        item.set(DataComponents.USE_COOLDOWN, new UseCooldown(TimeHandler.ticksToSeconds(cooldown)));
    }

    private void tryUpdateShardData(ItemStack itemStack, Entity entity) {
        if (!(entity instanceof Player player)) return;

        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        BaseCharacter character = data.getCharacter();
        if (character == null) {
            setDefaultShardData(itemStack);
            return;
        }

        AbilityComponent ability = itemStack.get(CSCComponents.ABILITY);
        if (ability == null) {
            setDefaultShardData(itemStack);
            return;
        }

        Identifier characterAbility = character.getShardAbility();
        if (characterAbility == null) {
            setDefaultShardData(itemStack);
            return;
        }

        Identifier shardAbility = ability.id();

        if (shardAbility.equals(characterAbility)) return;
        updateShardData(player, itemStack);
    }

    private static void setDefaultShardData(ItemStack itemStack) {
        itemStack.set(CSCComponents.ABILITY, DEFAULT_SHARD_DATA);
        itemStack.remove(DataComponents.USE_COOLDOWN);
    }
}
