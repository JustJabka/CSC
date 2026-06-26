package justjabka.csc.contents.ability.item;

import justjabka.csc.contents.ability.generic.BaseInstantAbility;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.data.CSCEntityTypeTagProvider;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCSounds;
import justjabka.csc.types.AbilityContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;
import java.util.function.Consumer;

public class MidasAbility extends BaseInstantAbility {
    private static final int GOLD_REWARD = 250;

    public MidasAbility(Identifier id, int duration, AbilityContext ctx) {
        super(id, duration, ctx);
    }

    @Override
    public void getDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.midas.description.1").withStyle(ChatFormatting.GRAY));

        Component goldReward = Component.literal(String.valueOf(GOLD_REWARD)).withStyle(ChatFormatting.YELLOW);
        textConsumer.accept(
                Component.translatable("item.csc.midas.description.2", goldReward).withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    public void onStart() {
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

    @Override
    public boolean canActivate(AbilityContext ctx) {
        Optional<LivingEntity> target = ctx.getTarget();

        return target.map(entity ->
                entity.is(CSCEntityTypeTagProvider.CAN_BE_TURNED_INTO_GOLD)
        ).orElse(false);
    }
}
