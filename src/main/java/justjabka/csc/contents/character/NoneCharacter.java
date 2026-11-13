package justjabka.csc.contents.character;

import net.minecraft.resources.Identifier;

public class NoneCharacter extends Character {

    public NoneCharacter(Properties properties) {
        super(properties);
    }

    @Override
    public Identifier getShardAbility() {
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
