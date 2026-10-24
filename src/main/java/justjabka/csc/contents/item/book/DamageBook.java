package justjabka.csc.contents.item.book;

import justjabka.csc.contents.component.BookComponent;
import justjabka.csc.registries.CSCAttributes;
import justjabka.csc.registries.CSCComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class DamageBook extends Item {
    public DamageBook(Properties properties) {
        super(properties
                .component(
                        CSCComponents.BOOK,
                        new BookComponent(1, Attributes.ATTACK_DAMAGE, Optional.of(CSCAttributes.DAMAGE_BOOK_BONUS))
                )
        );
    }
}