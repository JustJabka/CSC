package justjabka.csc.registries;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import justjabka.csc.CSC;
import justjabka.csc.contents.component.AbilityComponent;
import justjabka.csc.contents.gui.ShopMenu;
import justjabka.csc.payloads.ActivateTrinketPayload;
import justjabka.csc.payloads.ShopSyncContentPayload;
import justjabka.csc.types.ActivationType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class CSCPayloads {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Payloads");
        registerC2S();
        registerReceivers();
    }

    public static void registerC2S() {
        PayloadTypeRegistry.serverboundPlay().register(ActivateTrinketPayload.TYPE, ActivateTrinketPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ShopSyncContentPayload.TYPE, ShopSyncContentPayload.CODEC);
    }

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ActivateTrinketPayload.TYPE, activateTrinketPayload());
        
        ServerPlayNetworking.registerGlobalReceiver(ShopSyncContentPayload.TYPE, (payload, context) -> {
            String query = payload.query();

            if (query.length() > 50) return;

            context.server().execute(() -> {
                ServerPlayer player = context.player();

                if (!(player.containerMenu instanceof ShopMenu shopMenu)) return;

                shopMenu.changeSearchQuery(query);
            });
        });
    }

    private static ServerPlayNetworking.@NonNull PlayPayloadHandler<ActivateTrinketPayload> activateTrinketPayload() {
        return (payload, context) -> {
            ServerPlayer player = context.player();

            String targetSlot = payload.slotGroup();
            int slotOffset = payload.slotOffset();

            context.server().execute(() -> TrinketsApi.getAttachment(player).getAllEquipped().forEach(
                    tuple -> {
                        TrinketSlotAccess trinketsSlotAccess = tuple.getA();
                        ItemStack stack = tuple.getB();

                        String slotId = trinketsSlotAccess.slotType().getId();
                        int slotIndex = trinketsSlotAccess.index();

                        if (!trinketsSlotAccess.isValid()) return;
                        if (!slotId.equals(targetSlot)) return;
                        if (slotIndex != slotOffset) return;

                        AbilityComponent ability = stack.get(CSCComponents.ABILITY);
                        if (ability == null) return;

                        if (!ability.activationTypes().contains(ActivationType.TRINKET)) return;
                        ability.onTrinketUse(player, stack);
                    }
            ));
        };
    }
}
