package net.lalusko.createcogsinvaders.item.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TeslaCannonRenderer extends CustomRenderedItemModelRenderer {

    private static PartialModel partial(String path) {
        return new PartialModel(new net.minecraft.resources.ResourceLocation(CreateCogsInvadersMod.MOD_ID, "item/" + path));
    }

    public static final PartialModel BODY     = partial("tesla_cannon_body");
    public static final PartialModel COIL     = partial("tesla_cannon_coil");
    public static final PartialModel ELECTRIC = partial("tesla_cannon_electric");

    @Override
    protected void render(ItemStack stack, ItemDisplayContext ctx, PoseStack ps, MultiBufferSource buf,
                          int light, int overlay, CreateCustomRenderedItemModel model) {

        PartialItemModelRenderer r = new PartialItemModelRenderer(ps, buf, light, overlay);

        boolean inHand = switch (ctx) {
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND,
                 THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> true;
            default -> false;
        };

        // 1) Cuerpo (estático)
        r.render(BODY.get(), light);

        // 2) Coil: rotación procedural (idle -> activo) solo en mano
        ps.pushPose();
        if (inHand) {
            float t = AnimationTickHolder.getRenderTime();   // tiempo con partial ticks
            float spin = lerp(firingFactor(stack), 120f, 640f); // °/s
            float angle = (t * spin) % 360f;
            // Si tu pivote no es el origen, traduce aquí: ps.translate(px, py, pz);
            ps.mulPose(Axis.ZP.rotationDegrees(angle));
        }
        r.render(COIL.get(), light);
        ps.popPose();

        // 3) Overlay eléctrico en FULL_BRIGHT cuando "dispara"
        if (inHand && isFiring(stack)) {
            ps.pushPose();
            float s = 1.0f + 0.06f * flashScale();
            ps.scale(s, s, s);
            r.render(ELECTRIC.get(), LightTexture.FULL_BRIGHT); // emissive
            ps.popPose();
        }
    }

    private static boolean isFiring(ItemStack stack) {
        Player p = Minecraft.getInstance().player;
        if (p == null) return false;
        boolean cd    = p.getCooldowns().isOnCooldown(stack.getItem());
        boolean using = p.isUsingItem() && p.getUseItem().getItem() == stack.getItem();
        return cd || using;
    }

    // 0..1 basado en uso/cooldown para acelerar el coil
    private static float firingFactor(ItemStack stack) {
        Player p = Minecraft.getInstance().player;
        if (p == null) return 0f;
        if (p.isUsingItem() && p.getUseItem().getItem() == stack.getItem()) return 1f;
        float perc = p.getCooldowns().getCooldownPercent(stack.getItem(), 0); // 0..1
        return smoothstep(perc);
    }

    private static float lerp(float t, float a, float b) { return a + t * (b - a); }
    private static float smoothstep(float x) { x = Math.max(0f, Math.min(1f, x)); return x*x*(3f-2f*x); }

    private static float flashScale() {
        long gt = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
        float phase = (gt % 8) / 8f;
        return (float)Math.sin(phase * (float)Math.PI * 2f); // -1..1
    }
}
