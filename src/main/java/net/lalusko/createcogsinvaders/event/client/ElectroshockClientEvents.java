package net.lalusko.createcogsinvaders.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, value = Dist.CLIENT)
public class ElectroshockClientEvents {

    // Overlay tipo freezing (pulsante)
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        Player p = mc.player;
        if (!p.hasEffect(ModEffects.ELECTROSHOCK.get())) return;

        var gg = e.getGuiGraphics();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        long t = mc.level.getGameTime();
        float pulse = (float)(0.35 + 0.15 * Math.sin(t * 0.35)); // 0.35–0.5
        int a = (int)(pulse * 255);
        int col = new Color(0x42, 0xC5, 0xFF, a).getRGB();

        RenderSystem.disableDepthTest();
        gg.fill(0, 0, w, h, col); // velo azul eléctrico suave
        RenderSystem.enableDepthTest();
    }

    // Chispas eléctricas (client tick)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        var p = mc.player;
        if (!p.hasEffect(ModEffects.ELECTROSHOCK.get())) return;

        // Pequeñas chispas cerca del jugador, no cada tick
        if (p.tickCount % 6 == 0) {
            var r = mc.level.random;
            double ox = (r.nextDouble() - 0.5) * 0.6;
            double oy = r.nextDouble() * 0.8 + 0.2;
            double oz = (r.nextDouble() - 0.5) * 0.6;
            mc.level.addParticle(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,
                    p.getX() + ox, p.getY() + p.getBbHeight() * 0.5 + oy, p.getZ() + oz,
                    0, 0.02, 0);
        }
    }
}
