package net.lalusko.createcogsinvaders.event.client;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientOverlays {
    private static final ResourceLocation ELECTRO_VIGNETTE =
            new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "textures/screens/electroshock_screen.png");

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "electroshock_screen",
                (gui, gg, partial, width, height) -> {
                    var mc = Minecraft.getInstance();
                    if (mc.player == null || !mc.player.hasEffect(ModEffects.ELECTROSHOCK.get())) return;

                    float alpha = 0.55f; // fijo, sin parpadeo
                    RenderSystem.enableBlend();
                    RenderSystem.disableDepthTest();
                    RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

                    // ¡OJO a la firma!: x, y, destW, destH, u, v, regW, regH, texW, texH
                    gg.blit(ELECTRO_VIGNETTE, 0, 0, width, height, 0, 0, 256, 256, 256, 256);

                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                    RenderSystem.enableDepthTest();
                    RenderSystem.disableBlend();
                });
    }
}

