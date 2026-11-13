package justjabka.csc.contents.character;

import com.mojang.serialization.Codec;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import justjabka.csc.CSC;
import justjabka.csc.handlers.AttributeHandler;
import justjabka.csc.registries.CSCBuiltInRegistries;
import justjabka.csc.registries.CSCRegistries;
import justjabka.csc.types.ShardContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.*;
import java.util.function.Consumer;

public abstract class Character {
    public static final Codec<Holder<Character>> CODEC = CSCBuiltInRegistries.CHARACTER.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Character>> STREAM_CODEC = ByteBufCodecs.holderRegistry(CSCRegistries.CHARACTER);

    protected static final String ABILITY_SLOT_ID = "offhand/ability";

    protected final Identifier key;
    protected final String descriptionId;
    protected final Identifier icon;
    protected final List<Item> abilities;
    protected final Map<Holder<Attribute>, Double> baseAttributes;
    protected final Map<Holder<Attribute>, AttributeModifier> attributeModifiers;

    public Character(final Character.Properties properties) {
        this.key = properties.id.identifier();
        this.descriptionId = properties.effectiveDescriptionId();
        this.icon = properties.effectiveIcon();
        this.abilities = properties.effectiveAbilities();
        this.baseAttributes = properties.effectiveBaseAttributes();
        this.attributeModifiers = properties.effectiveAttributeModifiers();
    }

    public Component getDisplayName() {
        return Component.translatable(this.descriptionId);
    }

    public Identifier getDisplayIcon() {
        return this.icon;
    }

    public static Identifier getModifierKey(Identifier id) {
        return id.withPrefix("character.");
    }

    // Shard
    // TODO: move shard to a separate passive ability
    public abstract Identifier getShardAbility();
    public abstract int getShardCooldown();
    public abstract int getShardDuration();
    public void getShardDescription(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        textConsumer.accept(Component.translatable("item.csc.shard.description").withStyle(ChatFormatting.GRAY));
    }

    // Abilities
    private void updateAbilities(Player player) {
        TrinketAttachment trinketAttachment = TrinketsApi.getAttachment(player);

        clearAbilities(trinketAttachment);
        setAbilities(player, trinketAttachment);
    }

    private void setAbilities(Player player, TrinketAttachment trinketAttachment) {
        for (int i = 0; i < abilities.size(); i++) {
            TrinketSlotAccess slotAccess = trinketAttachment.getSlotAccess(ABILITY_SLOT_ID, i);

            if (slotAccess == null) return;
            if (!slotAccess.isValid()) return;

            Item ability = abilities.get(i);
            ItemStack abilityStack = ability.getDefaultInstance();

            // Get shit
            HolderLookup.RegistryLookup<Enchantment> enchLookup = player.registryAccess().lookupOrThrow(Enchantments.BINDING_CURSE.registryKey());
            Holder<Enchantment> bindingCurse = enchLookup.getOrThrow(Enchantments.BINDING_CURSE);

            SequencedSet<DataComponentType<?>> hiddenComponents = new LinkedHashSet<>();
            hiddenComponents.add(DataComponents.ENCHANTMENTS);

            // Apply shit
            abilityStack.enchant(bindingCurse, 1);
            abilityStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            abilityStack.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, hiddenComponents));

            slotAccess.set(abilityStack);
        }
    }

    private static void clearAbilities(TrinketAttachment trinketAttachment) {
        TrinketInventory trinketInventory = trinketAttachment.getInventory(ABILITY_SLOT_ID);
        if (trinketInventory == null) return;

        trinketInventory.clearContent();
    }

    // Attributes
    private void setBaseAttributes(Player player) {
        AttributeHandler.resetBaseValues(player);
        AttributeHandler.setBaseValues(player, this.baseAttributes);
    }

    private void applyAttributeModifiers(Player player) {
        AttributeHandler.removeAllModifiersFrom(player, CSC.MOD_ID, "character");
        AttributeHandler.addPermanentModifiers(player, attributeModifiers);
    }

    // Shard
    public void onShardTrigger(ShardContext ctx) {}

    // Other
    public void onSelect(Player player) {
        setBaseAttributes(player);
        applyAttributeModifiers(player);

        player.heal(player.getMaxHealth());

        updateAbilities(player);
    }

    public static class Properties {
        private static final DependantName<Character, String> CHARACTER_DESCRIPTION_ID = id -> Util.makeDescriptionId("character", id.identifier());
        private static final DependantName<Character, Identifier> CHARACTER_ICON = id -> {
            Identifier identifier = id.identifier();

            return Identifier.fromNamespaceAndPath(
                    identifier.getNamespace(),
                    "character/%s".formatted(identifier.getPath())
            );
        };

        private ResourceKey<Character> id;
        private DependantName<Character, String> descriptionId = CHARACTER_DESCRIPTION_ID;
        private DependantName<Character, Identifier> icon = CHARACTER_ICON;

        private List<Item> abilities = Collections.emptyList();

        private Map<Holder<Attribute>, AttributeModifier> attributeModifiers = Collections.emptyMap();
        private Map<Holder<Attribute>, Double> baseAttributes = Collections.emptyMap();

        public ResourceKey<Character> getId() {
            return this.id;
        }

        public Character.Properties setId(final ResourceKey<Character> id) {
            this.id = id;
            return this;
        }

        public Character.Properties overrideDescription(final String descriptionId) {
            this.descriptionId = DependantName.fixed(descriptionId);
            return this;
        }

        public Character.Properties overrideIcon(Identifier iconPath) {
            this.icon = DependantName.fixed(iconPath);
            return this;
        }

        public Character.Properties overrideAbilities(List<Item> abilities) {
            this.abilities = abilities;
            return this;
        }

        public Character.Properties overrideBaseAttributes(Map<Holder<Attribute>, Double> baseAttributes) {
            this.baseAttributes = baseAttributes;
            return this;
        }

        public Character.Properties overrideAttributeModifiers(Map<Holder<Attribute>, AttributeModifier> attributeModifiers) {
            this.attributeModifiers = attributeModifiers;
            return this;
        }

        public final ResourceKey<Character> characterIdOrThrow() {
            return Objects.requireNonNull(this.id, "Character id not set");
        }

        protected String effectiveDescriptionId() {
            return this.descriptionId.get(this.characterIdOrThrow());
        }

        public Identifier effectiveIcon() {
            return this.icon.get(this.characterIdOrThrow());
        }

        public List<Item> effectiveAbilities() {
            return this.abilities;
        }

        public Map<Holder<Attribute>, Double> effectiveBaseAttributes() {
            return this.baseAttributes;
        }

        public Map<Holder<Attribute>, AttributeModifier> effectiveAttributeModifiers() {
            return this.attributeModifiers;
        }
    }
}
