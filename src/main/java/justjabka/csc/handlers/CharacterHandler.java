package justjabka.csc.handlers;

import justjabka.csc.contents.attachement.PlayerData;
import justjabka.csc.contents.character.Character;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.registries.CSCBuiltInRegistries;
import justjabka.csc.registries.CSCCharacters;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CharacterHandler {

    private CharacterHandler() {}

    public static Character getCharacter(Player player) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        return data.getCharacter();
    }

    public static void setCharacter(Player player, ResourceKey<Character> key) {
        Character character = CSCBuiltInRegistries.CHARACTER.getValueOrThrow(key);
        setCharacter(player, character);
    }

    public static void setCharacter(Player player, Character character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        player.setAttached(
                CSCAttachments.PLAYER_DATA,
                data.setCharacter(character)
        );

        character.onSelect(player);
    }

    @Environment(EnvType.CLIENT)
    public static Character getClientCharacter() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return CSCCharacters.NONE;
        return getCharacter(player);
    }

    public static boolean isCurrentCharacter(Player player, Character character) {
        PlayerData data = player.getAttachedOrCreate(CSCAttachments.PLAYER_DATA);
        Character currentCharacter = data.getCharacter();

        return currentCharacter.equals(character);
    }
}
