package justjabka.csc.contents.item.generic;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseBook extends BaseConsumable {
    public BaseBook(Properties properties) {
        super(properties);
    }

    protected abstract double getBonusValue();
    protected abstract Holder<Attribute> getBonusAttribute();
    protected abstract Holder<Attribute> getBookBonusAttribute();

    @Override
    protected void onUse(Level level, Player player, InteractionHand hand, ItemStack stack) {
        // Get Attribute Value
        AttributeInstance attributeInstance = player.getAttribute(getBonusAttribute());
        if (attributeInstance == null) return;

        double attributeValue = attributeInstance.getBaseValue();

        // Apply Bonus
        attributeInstance.setBaseValue(attributeValue + calcBonusValue(player));

        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1f, 1f);
    }

    protected double calcBonusValue(Player player) {
        double bookBonus = 0;

        if (getBookBonusAttribute() != null) {
            AttributeInstance bookBonusInstance = player.getAttribute(getBookBonusAttribute());
            bookBonus = bookBonusInstance == null ? 0 : bookBonusInstance.getValue();
        }

        return getBonusValue() + bookBonus;
    }
}
