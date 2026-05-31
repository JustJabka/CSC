package justjabka.csc.data;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class CSCDamageTypeTagProvider extends FabricTagsProvider<DamageType> {
    public static final TagKey<DamageType> BYPASSES_DODGE = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(CSC.MOD_ID, "bypasses_dodge"));

    public CSCDamageTypeTagProvider(FabricPackOutput output,
                                    CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(BYPASSES_DODGE)
                .forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .add(DamageTypes.CRAMMING)
                .add(DamageTypes.DROWN)
                .add(DamageTypes.DRY_OUT)
                .add(DamageTypes.FREEZE)
                .add(DamageTypes.IN_WALL)
                .add(DamageTypes.OUTSIDE_BORDER)
                .add(DamageTypes.STARVE)
                .add(DamageTypes.WITHER);

        builder(DamageTypeTags.NO_KNOCKBACK)
                .add(DamageTypes.MAGIC)
                .add(DamageTypes.INDIRECT_MAGIC);

        builder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
                .add(DamageTypes.MAGIC)
                .add(DamageTypes.INDIRECT_MAGIC);
    }
}
