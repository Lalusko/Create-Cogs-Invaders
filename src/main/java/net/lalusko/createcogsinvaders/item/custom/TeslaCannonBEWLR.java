package net.lalusko.createcogsinvaders.item.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TeslaCannonBEWLR extends BlockEntityWithoutLevelRenderer {
    private static final String MOD_ID = "create_cogs_invaders"; // ← si tu modid es distinto, cámbialo aquí

    private BakedModel bodyModel;
    private BakedModel coilModel;
    private BakedModel flashModel;

    public TeslaCannonBEWLR() {
        super(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
        // Cargamos lazy en renderByItem para evitar timing issues de ModelManager
    }

    private void ensureModels() {
        if (bodyModel != null) return;
        var mm = Minecraft.getInstance().getModelManager();
        bodyModel  = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_body"), "inventory"));
        coilModel  = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_coil"), "inventory"));
        flashModel = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_electric"), "inventory"));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                             MultiBufferSource buf, int light, int overlay) {

        ensureModels();

        var mc    = Minecraft.getInstance();
        var level = mc.level;
        float pt  = mc.getFrameTime();
        long gt   = (level != null ? level.getGameTime() : 0);

        // Estado "on" basado en NBT (setéalo al disparar)
        long last = stack.getOrCreateTag().getLong("lastShot");
        float dt  = (float)((gt - last) + pt); // ticks desde el último disparo

        // Parámetros (ajusta a gusto)
        float onDur     = 6f;   // duración del “on” (ticks)
        float idleSpeed = 6f;   // grados/tick en idle
        float onSpeed   = 36f;  // grados/tick en “on”
        float rotSpeed  = (dt >= 0 && dt < onDur) ? onSpeed : idleSpeed;
        float angleDeg  = rotSpeed * (gt + pt);

        var renderer = mc.getItemRenderer();

        // 1) Render cuerpo (sin transform)
        renderer.render(stack, ctx, false, pose, buf, light, overlay, bodyModel);

        // 2) Render bobina: rotación sobre su pivote
        pose.pushPose();
        // Ajusta estos offset al pivote real que definiste en Blockbench para la bobina
        pose.translate(0.5, 0.5, 0.5);          // mover al centro (ejemplo)
        pose.mulPose(Axis.ZP.rotationDegrees(angleDeg)); // eje de giro: cambia a YP/XP si corresponde
        pose.translate(-0.5, -0.5, -0.5);
        renderer.render(stack, ctx, false, pose, buf, LightTexture.FULL_BRIGHT, overlay, coilModel); // fullbright para “brillo”
        pose.popPose();

        // 3) Render destello eléctrico: solo durante “on”
        if (dt >= 0 && dt < onDur) {
            pose.pushPose();
            // Posiciona el plane del destello en la punta de la antena (ajusta a tu modelo)
            pose.translate(0.5, 0.5, 0.9);
            // Si quieres animar la escala con el tiempo:
            // float s = 0.85f + 0.15f * (1f - dt/onDur); pose.scale(s, s, s);
            renderer.render(stack, ctx, false, pose, buf, LightTexture.FULL_BRIGHT, overlay, flashModel);
            pose.popPose();
        }
    }
}
