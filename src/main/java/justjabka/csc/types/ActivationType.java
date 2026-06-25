package justjabka.csc.types;

import net.minecraft.util.StringRepresentable;

public enum ActivationType implements StringRepresentable {
    GENERIC("generic"),
    INTERACTION("interaction"),
    TRINKET("trinket"),
    PASSIVE("passive"),
    BLOCK("block");

    private final String name;

    public static final StringRepresentable.EnumCodec<ActivationType> CODEC = StringRepresentable.fromEnum(ActivationType::values);

    ActivationType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
