package justjabka.csc.contents.character;

import justjabka.csc.contents.ability.Ability;
import net.minecraft.core.Holder;

public class NoneCharacter extends Character {

    public NoneCharacter(Properties properties) {
        super(properties);
    }

    @Override
    public Holder<Ability> getShardAbility() {
        return null;
    }

    @Override
    public int getShardCooldown() {
        return 0;
    }

    @Override
    public int getShardDuration() {
        return 0;
    }
}
