package justjabka.csc.handlers;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AbilityContext {
    public final Player player;
    public final @Nullable EquipmentSlot slot;
    public final Level level;

    private final @Nullable ItemStack item;
    private final @Nullable LivingEntity target;

    public java.util.Optional<EquipmentSlot> getSlot() { return java.util.Optional.ofNullable(slot); }

    public AbilityContext(Player player, EquipmentSlot slot) {
        this(player, slot, null, null);
    }

    public AbilityContext(Player player, EquipmentSlot slot, @Nullable ItemStack item, @Nullable LivingEntity target) {
        this.player = player;
        this.slot = slot;
        this.level = player.level();
        this.item = item;
        this.target = target;
    }

    public AbilityContext(Player player, ItemStack item) {
        this.player = player;
        this.slot = null;
        this.level = player.level();
        this.item = item;
        this.target = null;
    }

    public ItemStack getItem() {
        if (item != null) return item;
        if (slot != null) return player.getItemBySlot(slot);

        return ItemStack.EMPTY;
    }

    public Optional<LivingEntity> getTarget() {
        return Optional.ofNullable(target);
    }
}