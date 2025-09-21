package net.lalusko.createcogsinvaders.client;

import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.TESLA_SHIELD.get(),
                    new ResourceLocation("blocking"),
                    (stack, level, entity, seed) -> {
                        if (entity == null) return 0.0F;
                        return entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
                    }
            );
        });
    }
}
