package justjabka.csc.types;

import justjabka.csc.contents.ability.generic.ActiveAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AbilityType<T extends ActiveAbility> {
    private final Identifier id;
    private final AbilityFactory<T> factory;
    private final String translateKey;

    public AbilityType(Identifier id, AbilityFactory<T> factory) {
        this.id = id;
        this.factory = factory;
        this.translateKey = id.toLanguageKey("ability");
    }

    public Identifier getId() {
        return id;
    }

    public Component getName() {
        return Component.translatable(translateKey);
    }

    public T createInstance(int duration, AbilityContext ctx) {
        return factory.create(this.id, duration, ctx);
    }

    @FunctionalInterface
    public interface AbilityFactory<T extends ActiveAbility> {
        T create(Identifier id, int duration, AbilityContext ctx);
    }
}
