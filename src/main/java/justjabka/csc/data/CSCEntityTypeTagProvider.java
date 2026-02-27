package justjabka.csc.data;

import justjabka.csc.CSC;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class CSCEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public static final TagKey<EntityType<?>> CAN_BE_TURNED_INTO_GOLD = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CSC.MOD_ID, "can_be_turned_into_gold"));

    public CSCEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(CAN_BE_TURNED_INTO_GOLD)
                .add(EntityType.ZOMBIE)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.SPIDER)
                .add(EntityType.CAVE_SPIDER)
                .add(EntityType.CREEPER)
                .add(EntityType.WOLF)
                .add(EntityType.SILVERFISH)
                .add(EntityType.ENDERMITE)
                .add(EntityType.WITCH)
                .add(EntityType.VINDICATOR)
                .add(EntityType.EVOKER)
                .add(EntityType.VEX)
                .add(EntityType.BLAZE);
    }
}
