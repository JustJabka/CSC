package justjabka.csc.contents.ability;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import justjabka.csc.CSC;
import justjabka.csc.registries.CSCBuiltInRegistries;
import justjabka.csc.registries.CSCRegistries;
import justjabka.csc.types.AbilityCategory;
import justjabka.csc.types.AbilityContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Ability {
    public static final Codec<Holder<Ability>> CODEC = CSCBuiltInRegistries.ABILITY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Ability>> STREAM_CODEC = ByteBufCodecs.holderRegistry(CSCRegistries.ABILITY);

    private @Nullable String descriptionId;
    private final AbilityCategory category;
    private final Map<Holder<Attribute>, AttributeModifier> attributeModifiers = new Object2ObjectOpenHashMap<>();

    public Ability(AbilityCategory category) {
        this.category = category;
    }

    public void onStart(AbilityContext ctx) {}
    public boolean onTick(AbilityContext ctx) {
        return true;
    }
    public void onEnd(AbilityContext ctx) {}

    public boolean canActivate(AbilityContext ctx) {
        return true;
    }

    public boolean shouldEnd(AbilityContext ctx) {
        return false;
    }

    public int tickDuration(int duration) {
        return duration - 1;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("ability", CSCBuiltInRegistries.ABILITY.getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getDisplayName() {
        return Component.translatable(this.getDescriptionId());
    }

    @Environment(EnvType.CLIENT)
    public void displayDescription(
            Item.TooltipContext context,
            Consumer<Component> textConsumer,
            TooltipFlag type,
            DataComponentGetter components
    ) {}

    public AbilityCategory getCategory() {
        return category;
    }

    public void addAttributeModifier(final Holder<Attribute> attribute, final double amount, final AttributeModifier.Operation operation) {
        final Identifier id = CSC.id(getDescriptionId() + "." + attributeModifiers.size());

        this.attributeModifiers.put(attribute, new AttributeModifier(id, amount, operation));
    }

    public void createModifiers(final BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        this.attributeModifiers.forEach(consumer);
    }

    public void removeAttributeModifiers(final AttributeMap attributes) {
        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attribute = attributes.getInstance(entry.getKey());
            if (attribute != null) {
                attribute.removeModifier(entry.getValue().id());
            }
        }
    }

    public void addAttributeModifiers(final AttributeMap attributes) {
        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : this.attributeModifiers.entrySet()) {
            AttributeInstance attribute = attributes.getInstance(entry.getKey());
            if (attribute != null) {
                attribute.removeModifier(entry.getValue().id());
                attribute.addTransientModifier(entry.getValue());
            }
        }
    }
}
