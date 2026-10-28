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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CharacterHandler {

    private CharacterHandler() {}

    public static @Nullable BaseCharacter getCharacter(Player player) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        return data.getCharacter();
    }

    public static void setCharacter(Player player, Identifier key) {
        BaseCharacter character = CSCCharacters.getOrThrow(key);
        setCharacter(player, character);
    }

    public static void setCharacter(Player player, BaseCharacter character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.setCharacter(character.getKey())
        );

        character.onSelect(player);
    }

    @Environment(EnvType.CLIENT)
    public static @Nullable BaseCharacter getClientCharacter() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return null;
        return getCharacter(player);
    }

    public static boolean isCurrentCharacter(Player player, Identifier character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        BaseCharacter currentCharacter = data.getCharacter();

        if (currentCharacter == null) return false;
        return currentCharacter.getKey().equals(character);
    }

    public static boolean isCurrentCharacter(Player player, BaseCharacter character) {
        return isCurrentCharacter(player, character.getKey());
    }
}
