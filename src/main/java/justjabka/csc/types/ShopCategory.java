package justjabka.csc.types;

import justjabka.csc.CSC;
import net.minecraft.network.chat.Component;

public enum ShopCategory {
    DAMAGE,
    MAGIC,
    SURVIVABILITY,
    ANTI_MAGE,
    TACTIC;

    private final String id;
    private final String translationKey;

    ShopCategory() {
        this.id = this.name().toLowerCase();
        this.translationKey = "shop_category.%s.%s".formatted(CSC.MOD_ID, this.id);
    }

    public String getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }
}
