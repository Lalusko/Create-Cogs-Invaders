package net.lalusko.createcogsinvaders.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lalusko.createcogsinvaders.item.custom.TeslaCannonItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TeslaCannonClientEvents {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent e) {
        ItemStack stack = e.getItemStack();
        if (!(stack.getItem() instanceof TeslaCannonItem)) return;

        var player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean firing = player.getCooldowns().isOnCooldown(stack.getItem()) ||
                (player.isUsingItem() && player.getUseItem().getItem() == stack.getItem());

        if (!firing) return;

        PoseStack ps = e.getPoseStack();
        float kick = 0.6f;
        ps.translate(0.0f, -0.02f * kick, -0.08f * kick);
        ps.mulPose(Axis.XP.rotationDegrees(3.5f * kick));
    }
}
