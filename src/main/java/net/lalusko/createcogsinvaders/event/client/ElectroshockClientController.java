package net.lalusko.createcogsinvaders.event.client;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElectroshockClientController {
    private static float alpha = 0f;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.player.isSpectator()) {
            pushToOverlay(0f);
            return;
        }
        boolean active = mc.player.hasEffect(ModEffects.ELECTROSHOCK.get());
        float target = 0f;
        if (active) {
            long t = mc.level.getGameTime();
            target = (float)(0.35 + 0.15 * Math.sin(t * 0.35)); // pulso 0.35–0.50
        }
        alpha = Mth.lerp(0.15f, alpha, target);
        pushToOverlay(alpha);
    }

    // TODO: Cambia estas llamadas por tu API real del overlay
    private static void pushToOverlay(float a) {
        // YourElectroOverlay.setEnabled(a > 0.02f);
        // YourElectroOverlay.setAlpha(a);
    }
}

