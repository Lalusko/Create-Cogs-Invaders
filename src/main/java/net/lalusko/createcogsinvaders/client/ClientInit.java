package net.lalusko.createcogsinvaders.client;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.item.alchemy.PotionUtils;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            // layer0 (vidrio) sin tinte; layer1 (líquido) usa color de poción
            return tintIndex == 1 ? PotionUtils.getColor(stack) : 0xFFFFFF;
        }, ModItems.LARGE_BOTTLE.get());
    }
}
