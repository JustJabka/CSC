package justjabka.csc.data;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class CSCEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public static final TagKey<EntityType<?>> CAN_BE_TURNED_INTO_GOLD = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CSC.MOD_ID, "can_be_turned_into_gold"));

    public CSCEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(CAN_BE_TURNED_INTO_GOLD)
                .add(EntityTypeIds.ZOMBIE)
                .add(EntityTypeIds.SKELETON)
                .add(EntityTypeIds.WITHER_SKELETON)
                .add(EntityTypeIds.SPIDER)
                .add(EntityTypeIds.CAVE_SPIDER)
                .add(EntityTypeIds.CREEPER)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.SILVERFISH)
                .add(EntityTypeIds.ENDERMITE)
                .add(EntityTypeIds.WITCH)
                .add(EntityTypeIds.VINDICATOR)
                .add(EntityTypeIds.EVOKER)
                .add(EntityTypeIds.VEX)
                .add(EntityTypeIds.BLAZE);
    }
}
