package justjabka.csc.contents.ability.generic;

import justjabka.csc.contents.attachement.AbilitiesData;
import justjabka.csc.registries.CSCAttachments;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.types.AbilityData;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public abstract class BaseActiveAbility {
    protected final Identifier id;
    protected int duration;
    protected final int maxDuration;
    protected final AbilityContext ctx;
    protected boolean ended = false;

    public BaseActiveAbility(Identifier id, int duration, AbilityContext ctx) {
        this.id = id;
        this.duration = duration;
        this.maxDuration = duration;
        this.ctx = ctx;
    }

    // Getters
    public Identifier getId() {
        return this.id;
    }

    public Identifier getIcon() {
        Item item = ctx.getItem().getItem();
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public abstract void getDescription(
            Item.TooltipContext context,
            Consumer<Component> textConsumer,
            TooltipFlag type,
            DataComponentGetter components
    );

    public void start() {
        onStart();
        saveAbilitiesData();
    }

    public void tick() {
        this.duration = updateDuration();
        onTick();
        saveAbilitiesData();
    }

    public final void end() {
        onEnd();

        AbilitiesData data = getAbilitiesData();
        ctx.player.setAttached(CSCAttachments.ABILITIES_DATA, data.removeAbility(getId()));
    }

    // Time
    public void refresh(BaseActiveAbility ability) {
        this.duration = ability.duration;
        saveAbilitiesData();
    }
    protected int updateDuration() {
        return --duration;
    }

    // End
    public boolean isEnded() {
        return this.ended || duration <= 0;
    }
    public void forceEnd() {
        this.ended = true;
    }
    public boolean shouldEnd() {
        return false;
    }

    // Other
    public boolean canActivate(AbilityContext ctx) {
        return true;
    }
    public boolean isPlayerValid() {
        return !ctx.player.isDeadOrDying();
    }

    // Entrypoint
    public abstract void onStart();
    public abstract void onTick();
    public abstract void onEnd();

    // Abilities Data
    protected AbilitiesData getAbilitiesData() {
        return ctx.player.getAttachedOrCreate(CSCAttachments.ABILITIES_DATA);
    }

    protected void saveAbilitiesData() {
        AbilitiesData data = getAbilitiesData();
        AbilityData abilityData = new AbilityData(this.duration, this.maxDuration, getIcon());
        ctx.player.setAttached(CSCAttachments.ABILITIES_DATA, data.updateAbility(getId(), abilityData));
    }
}