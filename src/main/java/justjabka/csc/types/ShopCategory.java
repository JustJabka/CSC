package justjabka.csc.types;

import justjabka.csc.CSC;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum ShopCategory {
    DAMAGE(Items.IRON_SWORD.getDefaultInstance()),
    MAGIC(Items.ENDER_EYE.getDefaultInstance()),
    SURVIVABILITY(Items.SHIELD.getDefaultInstance()),
    ANTI_MAGE(Items.MILK_BUCKET.getDefaultInstance()),
    TACTIC(Items.LIGHT.getDefaultInstance());

    private final String id;
    private final String translationKey;
    private final ItemStack icon;

    ShopCategory(ItemStack icon) {
        this.id = this.name().toLowerCase();
        this.translationKey = "shop_category.%s.%s".formatted(CSC.MOD_ID, this.id);
        this.icon = icon;
    }

    public String getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }

    public ItemStack getIcon() {
        return icon;
    }
}
