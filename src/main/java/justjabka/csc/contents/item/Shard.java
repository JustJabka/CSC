package justjabka.csc.contents.item;

import justjabka.csc.CSC;
import justjabka.csc.contents.ability.generic.BaseActiveAbility;
import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.registries.CSCAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class Shard extends BaseActiveTrinketItem {
    public Shard(Properties properties) {
        super(properties);
    }

    @Override
    protected Identifier getKey() {
        return Identifier.fromNamespaceAndPath(CSC.MOD_ID, "shard");
    }

    @Override
    protected int getCooldown() {
        return 0;
    }

    @Override
    protected int getDuration() {
        return 0;
    }

    @Override
    protected BaseActiveAbility getAbility() {
        return null;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay displayComponent, Consumer<Component> textConsumer, @NonNull TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        BaseCharacter character = data.getCharacter();
        if (character == null) return;

        character.getShardDescription(stack, context, displayComponent, textConsumer, type);
    }
}
