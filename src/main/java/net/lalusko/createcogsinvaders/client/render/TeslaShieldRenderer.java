package net.lalusko.createcogsinvaders.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TeslaShieldRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation EMISSIVE_RING =
            new ResourceLocation("create_cogs_invaders", "textures/item/tesla_shield_blocking_emissive.png");

    public TeslaShieldRenderer(net.minecraft.world.level.block.entity.BlockEntityRenderDispatcher berd, EntityModelSet set) {
        super(berd, set);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {

        var mc = Minecraft.getInstance();
        ItemRenderer ir = mc.getItemRenderer();
        BakedModel model = ir.getModel(stack, null, null, 0);

        // 1) Render normal
        ir.render(stack, ctx, false, pose, buffers, packedLight, packedOverlay, model);

        // 2) ¿Está bloqueando?
        boolean blocking = false;
        LivingEntity viewer = mc.player;
        if (viewer != null && viewer.isUsingItem() && viewer.getUseItem() == stack) {
            blocking = true;
        }

        if (!blocking) return;

        // 3) Dibujar capa emissive (fullbright) como overlay
        pose.pushPose();

        // Opcional: rotación/flicker suave
        float time = (mc.level != null ? mc.level.getGameTime() : 0) + mc.getFrameTime();
        float angleDeg = (time * 6f) % 360f; // gira lento
        float alpha   = 0.80f + 0.20f * (float)Math.sin(time * 0.25f); // leve “respirar”

        // Ajusta escala/posición según tu modelo
        pose.mulPose(Axis.ZP.rotationDegrees(angleDeg));
        float s = 1.0f; // escala: 1 = tamaño del item
        pose.scale(s, s, s);

        var matrix = pose.last().pose();
        var vb = buffers.getBuffer(RenderType.eyes(EMISSIVE_RING)); // fullbright

        int a = (int)(alpha * 255f);
        // Quad plano muy cerca del frente para evitar z-fighting
        float z = 0.001f;

        vb.vertex(matrix, -1f, -1f, z, 0f, 1f).uv2(LightTexture.FULL_BRIGHT).overlayCoords(OverlayTexture.NO_OVERLAY).color(255,255,255,a).endVertex();
        vb.vertex(matrix,  1f, -1f, z, 1f, 1f).uv2(LightTexture.FULL_BRIGHT).overlayCoords(OverlayTexture.NO_OVERLAY).color(255,255,255,a).endVertex();
        vb.vertex(matrix,  1f,  1f, z, 1f, 0f).uv2(LightTexture.FULL_BRIGHT).overlayCoords(OverlayTexture.NO_OVERLAY).color(255,255,255,a).endVertex();
        vb.vertex(matrix, -1f,  1f, z, 0f, 0f).uv2(LightTexture.FULL_BRIGHT).overlayCoords(OverlayTexture.NO_OVERLAY).color(255,255,255,a).endVertex();

        pose.popPose();
    }
}
