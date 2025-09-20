package net.lalusko.createcogsinvaders.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lalusko.createcogsinvaders.item.custom.TeslaCannonItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TeslaCannonClientEvents {

    // Quita el "manotazo" en 3ª (y 1ª) cuando lleva el Tesla Cannon
    @SubscribeEvent
    public static void onSwing(LivingSwingEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        ItemStack main = p.getMainHandItem();
        if (main.getItem() instanceof TeslaCannonItem) {
            e.setCanceled(true);
        }
    }

    // Ajuste de pose en 1ª persona (centrar/apuntar tipo Potato Cannon)
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent e) {
        ItemStack stack = e.getItemStack();
        if (!(stack.getItem() instanceof TeslaCannonItem)) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean aiming = player.isUsingItem() && player.getUseItem().getItem() == stack.getItem();
        boolean firing = player.getCooldowns().isOnCooldown(stack.getItem());
        if (!(aiming || firing)) return;

        PoseStack ps = e.getPoseStack();

        // Mano derecha: mueve hacia centro y ligeramente atrás
        // Ajusta estos valores hasta que "sepa a Create"
        ps.translate(-0.25f, -0.15f, -0.15f);
        ps.mulPose(Axis.YP.rotationDegrees(-12f));
        ps.mulPose(Axis.XP.rotationDegrees(-6f));

        // Recoil suave si está en cooldown
        if (firing) {
            float kick = 0.6f;
            ps.translate(0.0f, -0.02f * kick, -0.08f * kick);
            ps.mulPose(Axis.XP.rotationDegrees(3.5f * kick));
        }
    }
}
