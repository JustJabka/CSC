package justjabka.csc.types;

import justjabka.csc.contents.character.generic.BaseCharacter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ShardContext {
    public final Player player;
    public final ItemStack shardStack;
    public final @Nullable BaseCharacter character;

    public ShardContext(Player player, ItemStack shardStack, @Nullable BaseCharacter character) {
        this.player = player;
        this.shardStack = shardStack;
        this.character = character;
    }
}
