package justjabka.csc.contents.item.book;

import justjabka.csc.contents.component.BookComponent;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class HealthBook extends Item {
    public HealthBook(Item.Properties properties) {
        super(properties
                .component(
                        CSCComponents.BOOK,
                        new BookComponent(2, Attributes.MAX_HEALTH, Optional.of(CSCAttributes.HEALTH_BOOK_BONUS))
                )
        );
    }
}