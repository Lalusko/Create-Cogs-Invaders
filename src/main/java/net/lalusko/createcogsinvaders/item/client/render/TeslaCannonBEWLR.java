package net.lalusko.createcogsinvaders.item.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TeslaCannonBEWLR extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    private ModelManager modelManager;

    private BakedModel body, coil, electric;

    public TeslaCannonBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
        this.modelManager = Minecraft.getInstance().getModelManager();
        reloadModels();
    }

    private void reloadModels() {
        body = modelManager.getModel(new ModelResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_body", "inventory"));
        coil = modelManager.getModel(new ModelResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_coil", "inventory"));
        electric = modelManager.getModel(new ModelResourceLocation(CreateCogsInvadersMod.MOD_ID, "tesla_cannon_electric", "inventory"));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        if (modelManager != Minecraft.getInstance().getModelManager()) {
            modelManager = Minecraft.getInstance().getModelManager();
            reloadModels();
        }

        boolean inHand = switch (ctx) {
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> true;
            default -> false;
        };
        boolean leftHand = (ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND);

        itemRenderer.render(stack, ctx, leftHand, ps, buf, light, overlay, body);

        ps.pushPose();
        if (inHand) {
            long gt = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
            float angle = (gt % 360) * 10f;
            ps.mulPose(Axis.ZP.rotationDegrees(angle));
        }
        itemRenderer.render(stack, ctx, leftHand, ps, buf, light, overlay, coil);
        ps.popPose();

        if (inHand && isFiring(stack)) {
            ps.pushPose();
            float s = flashScale();
            ps.scale(1.0f + 0.1f * s, 1.0f + 0.1f * s, 1.0f + 0.1f * s);
            int glow = 0xF000F0;
            itemRenderer.render(stack, ctx, leftHand, ps, buf, glow, overlay, electric);
            ps.popPose();
        }
    }

    private boolean isFiring(ItemStack stack) {
        Player p = Minecraft.getInstance().player;
        if (p == null) return false;
        boolean cd = p.getCooldowns().isOnCooldown(stack.getItem());
        boolean using = p.isUsingItem() && p.getUseItem().getItem() == stack.getItem();
        return cd || using;
    }

    private float flashScale() {
        long t = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
        float phase = (t % 8) / 8.0f; // 0..1 en 8 ticks
        return (float) Math.sin(phase * Math.PI); // 0→1→0
    }
}
