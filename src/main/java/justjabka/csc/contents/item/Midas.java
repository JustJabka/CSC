package justjabka.csc.contents.item;

import justjabka.csc.contents.ability.generic.ActiveAbility;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.item.generic.BaseActiveItem;
import justjabka.csc.data.CSCEntityTypeTagProvider;
import justjabka.csc.handlers.ActiveItemConfig;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

import java.util.function.Consumer;

public class Midas extends BaseActiveItem {
    // Item Properties
    private static final int GOLD_REWARD = 250;

    public Midas(Properties properties) {
        super(properties
                .rarity(Rarity.UNCOMMON),
                new ActiveItemConfig(
                        true,
                        false,
                        100,
                        0
                )
        );
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        textConsumer.accept(Component.translatable("other.csc.cooldown", config.cooldown).withStyle(ChatFormatting.YELLOW));
        textConsumer.accept(Component.translatable("item.csc.midas.description").withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected ActiveAbility createAbility() {
        return null;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    protected boolean canActivate(Player player, InteractionHand hand, LivingEntity target, ItemStack stack) {
        return target.getType().is(CSCEntityTypeTagProvider.CAN_BE_TURNED_INTO_GOLD);
    }

    @Override
    protected void onUse(Player player, InteractionHand hand, LivingEntity target, ItemStack stack) {
        // Turn target into gold
        target.hurt(target.damageSources().magic(), Float.MAX_VALUE);

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