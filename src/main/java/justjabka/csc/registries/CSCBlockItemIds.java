package justjabka.csc.registries;

import justjabka.csc.CSC;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class CSCBlockItemIds {
    public static final BlockItemId SHOP = create("shop");
    public static final BlockItemId UPGRADE = create("upgrade");

    private static BlockItemId create(String name) {
        Identifier id = CSC.id(name);
        return BlockItemId.create(id, id);
    }
}
