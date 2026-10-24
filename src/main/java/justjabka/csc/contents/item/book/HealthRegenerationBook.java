package justjabka.csc.contents.item.book;

import justjabka.csc.contents.component.BookComponent;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCComponents;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class HealthRegenerationBook extends Item {
    public HealthRegenerationBook(Item.Properties properties) {
        super(properties
                .component(
                        CSCComponents.BOOK,
                        new BookComponent(1, CSCAttributes.HEALTH_REGENERATION, Optional.of(CSCAttributes.HEALTH_REGENERATION_BOOK_BONUS))
                )
        );
    }
}