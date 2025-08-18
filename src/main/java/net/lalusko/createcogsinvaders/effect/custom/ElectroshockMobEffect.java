package net.lalusko.createcogsinvaders.effect.custom;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class ElectroshockMobEffect extends MobEffect {
    // UUIDs estables para que no se dupliquen
    private static final UUID MOVE_UUID = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-111111111111");
    private static final UUID ATKSPD_UUID = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-222222222222");
    private static final UUID ATKDAM_UUID = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-333333333333");

    public ElectroshockMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x42C5FF); // color azul eléctrico
        // −50% Movimiento
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVE_UUID.toString(),
                -0.50D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // −25% Velocidad de ataque
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ATKSPD_UUID.toString(),
                -0.33D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // −33% Daño de ataque
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATKDAM_UUID.toString(),
                -0.33D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    // No necesitamos ticks de “poison/regen”, los eventos harán el resto
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientMobEffectExtensions> consumer) {
        final net.minecraft.resources.ResourceLocation ICON =
                new net.minecraft.resources.ResourceLocation(CreateCogsInvadersMod.MOD_ID, "textures/mob_effect/electroshock.png");

        consumer.accept(new net.minecraftforge.client.extensions.common.IClientMobEffectExtensions() {

            @Override public boolean isVisibleInInventory(net.minecraft.world.effect.MobEffectInstance inst) { return true; }
            @Override public boolean isVisibleInGui(net.minecraft.world.effect.MobEffectInstance inst)       { return true; }

            // Inventario (24x24) -> centra 18x18 en (x+3,y+3)
            @Override
            public boolean renderInventoryIcon(net.minecraft.world.effect.MobEffectInstance inst,
                                               net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen<?> screen,
                                               net.minecraft.client.gui.GuiGraphics g, int x, int y, int blitZ) {
                g.blit(ICON, x + 3, y + 3, 0, 0, 18, 18, 18, 18);
                return true;
            }

            // HUD (slot 24x24 normalmente). También centrado en (x+3,y+3)
            @Override
            public boolean renderGuiIcon(net.minecraft.world.effect.MobEffectInstance inst,
                                         net.minecraft.client.gui.Gui gui,
                                         net.minecraft.client.gui.GuiGraphics g, int x, int y, float z, float alpha) {
                // ver punto 3 para el parpadeo (alpha)
                com.mojang.blaze3d.systems.RenderSystem.enableBlend();
                com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
                g.blit(ICON, x + 3, y + 3, 0, 0, 18, 18, 18, 18);
                com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                com.mojang.blaze3d.systems.RenderSystem.disableBlend();
                return true;
            }
        });
    }
}
