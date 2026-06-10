package justjabka.csc.registries;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import justjabka.csc.CSC;
import justjabka.csc.contents.item.generic.BaseActiveTrinketItem;
import justjabka.csc.types.AbilityContext;
import justjabka.csc.payloads.ActivateTrinketPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CSCPayloads {
    public static void initialize() {
        CSC.LOGGER.info("Initializing Payloads");
        registerC2S();
        registerReceivers();
    }

    public static void registerC2S() {
        PayloadTypeRegistry.serverboundPlay().register(ActivateTrinketPayload.TYPE, ActivateTrinketPayload.CODEC);
    }

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(ActivateTrinketPayload.TYPE, (payload, context) -> {
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

                        if (!(stack.getItem() instanceof BaseActiveTrinketItem activeItem)) return;

                        AbilityContext ctx = new AbilityContext(player, stack);
                        activeItem.tryActivate(ctx);
                    }
            ));
        });
    }
}
