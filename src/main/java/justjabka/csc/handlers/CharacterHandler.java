package justjabka.csc.handlers;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.generic.BaseCharacter;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCCharacters;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public class CharacterHandler {
    public static void setCharacter(Player player, Identifier character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.setCharacter(character)
        );

        BaseCharacter characterObj = CSCCharacters.getByKey(character);

        if (characterObj != null) {
            characterObj.onSelect(player);
        } else {
            AttributeHandler.resetBaseValues(player);
        }
    }

    @Environment(EnvType.CLIENT)
    public static @Nullable BaseCharacter getClientCharacter() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return null;
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);

        return data.getCharacter();
    }

    public static boolean isCurrentCharacter(Player player, Identifier character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        BaseCharacter currentCharacter = data.getCharacter();

        if (currentCharacter == null) return false;
        return currentCharacter.getKey().equals(character);
    }
}
