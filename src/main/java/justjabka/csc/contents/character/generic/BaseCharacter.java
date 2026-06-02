package justjabka.csc.contents.character.generic;

import justjabka.csc.CSC;
import justjabka.csc.handlers.AttributeHandler;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public abstract class BaseCharacter {
    public abstract Identifier getKey();
    public abstract Identifier getDisplayIcon();
    public abstract Map<Holder<Attribute>, Double> getBaseAttributes();

    public Component getDisplayName() {
        String key = "character.%s".formatted(getKey());
        return Component.translatableWithFallback(key, "Sorry the translate broke :(");
    }

    public void setBaseAttributes(Player player) {
        AttributeHandler.resetBaseValues(player);
        AttributeHandler.setBaseValues(player, getBaseAttributes());
    }

    public void onSelect(Player player) {
        setBaseAttributes(player);
        CSC.LOGGER.info("Selected character {}", this.getKey());
    }
}
