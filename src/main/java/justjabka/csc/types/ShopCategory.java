package justjabka.csc.types;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public enum ShopCategory implements StringRepresentable {
    DAMAGE("damage", () -> Items.IRON_SWORD),
    MAGIC("magic", () -> Items.ENDER_EYE),
    SURVIVABILITY("survivability", () -> Items.SHIELD),
    ANTI_MAGE("anti_mage", () -> Items.MILK_BUCKET),
    TACTIC("tactic", () -> Items.LIGHT);

    private final String name;
    private final Supplier<Item> icon;

    public static final StringRepresentable.EnumCodec<ShopCategory> CODEC = StringRepresentable.fromEnum(ShopCategory::values);

    ShopCategory(String name, Supplier<Item> icon) {
        this.name = name;
        this.icon = icon;
    }

    public ItemStack getIcon() {
        return this.icon.get().getDefaultInstance();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
