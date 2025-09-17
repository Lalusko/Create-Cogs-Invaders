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
    private static final String MOD_ID = CreateCogsInvadersMod.MOD_ID;

    private BakedModel bodyModel;
    private BakedModel coilModel;
    private BakedModel flashModel;

    public TeslaCannonBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        System.out.println("[TeslaCannonBEWLR] ctor");
    }

    private void ensureModels() {
        if (bodyModel != null) return;
        var mm = Minecraft.getInstance().getModelManager();
        bodyModel  = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_body"), "inventory"));
        coilModel  = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_coil"), "inventory"));
        flashModel = mm.getModel(new ModelResourceLocation(new ResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_electric"), "inventory"));

        var missing = Minecraft.getInstance().getModelManager().getMissingModel();
        if (bodyModel == missing) System.out.println("[TeslaCannonBEWLR] BODY = MISSING");
        if (coilModel == missing) System.out.println("[TeslaCannonBEWLR] COIL = MISSING");
        if (flashModel == missing) System.out.println("[TeslaCannonBEWLR] FLASH = MISSING");

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                             MultiBufferSource buf, int light, int overlay) {
        System.out.println("[TeslaCannonBEWLR] renderByItem");

        ensureModels();
        var mc = Minecraft.getInstance();
        var level = mc.level;
        float pt = mc.getFrameTime();
        long gt = (level != null ? level.getGameTime() : 0);

        long last = stack.getOrCreateTag().getLong("lastShot");
        float dt = (float)((gt - last) + pt);

        float onDur = 6f;
        float idleSpeed = 6f, onSpeed = 36f;
        float speed = (dt >= 0 && dt < onDur) ? onSpeed : idleSpeed;
        float angle = speed * (gt + pt);

        var renderer = mc.getItemRenderer();

        // cuerpo
        renderer.render(stack, ctx, false, pose, buf, light, overlay, bodyModel);

        // bobina (ajusta pivote/eje según tu Blockbench)
        pose.pushPose();
        pose.translate(0.5, 0.5, 0.5);
        pose.mulPose(Axis.ZP.rotationDegrees(angle));
        pose.translate(-0.5, -0.5, -0.5);
        renderer.render(stack, ctx, false, pose, buf, LightTexture.FULL_BRIGHT, overlay, coilModel);
        pose.popPose();

        // destello visible en "on"
        if (dt >= 0 && dt < onDur) {
            pose.pushPose();
            pose.translate(0.5, 0.5, 0.9); // ajusta a la punta de tu antena
            renderer.render(stack, ctx, false, pose, buf, LightTexture.FULL_BRIGHT, overlay, flashModel);
            pose.popPose();
        }
    }
}
