package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.component.ShopItemComponent;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.data.CSCEntityTypeTagProvider;
import justjabka.csc.handlers.TimeHandler;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCComponents;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.ShopCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.function.Consumer;

public class Midas extends BaseActiveItem {
    private static final int GOLD_REWARD = 250;

    @Override
    public Identifier getKey() {
        return null;
    }

    @Override
    public int getCooldown() {
        return TimeHandler.secondsToTicks(100);
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public BaseActiveAbility getAbility() {
        return null;
    }

    public Midas(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON)
                .component(CSCComponents.SHOP_ITEM, new ShopItemComponent(1250, ShopCategory.MAGIC))
        );
    }

    @Override
    public boolean canActivate(AbilityContext ctx) {
        Optional<LivingEntity> target = ctx.getTarget();

        return target.map(entity ->
                entity.is(CSCEntityTypeTagProvider.CAN_BE_TURNED_INTO_GOLD)
        ).orElse(false);
    }

    @Override
    public void onUse(AbilityContext ctx) {
        if (ctx.getTarget().isEmpty()) return;

        Player player = ctx.player;
        LivingEntity target = ctx.getTarget().get();

        target.kill((ServerLevel) ctx.level);

        // Add Gold
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.addGold(GOLD_REWARD)
        );

        // Play Sound
        player.level().playSound(null, target.blockPosition(), CSCSounds.ITEM_MIDAS, SoundSource.PLAYERS, 1f, 1f);
    }
}