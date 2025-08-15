package net.lalusko.createcogsinvaders.particle;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientParticles {
    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent e) {
        e.registerSpriteSet(ModParticles.HEALING_PARTICLES.get(), HealingParticles.Provider::new);
    }
}
