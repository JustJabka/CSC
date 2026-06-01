package justjabka.csc.contents.item.generic;

import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseActiveTrinketItem extends BaseActiveItem implements TrinketCallback {
    public BaseActiveTrinketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack item,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        return InteractionResult.PASS;
    }
}
