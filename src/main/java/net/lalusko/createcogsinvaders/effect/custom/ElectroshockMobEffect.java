package net.lalusko.createcogsinvaders.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class ElectroshockMobEffect extends MobEffect {
    // UUIDs estables para que no se dupliquen
    private static final UUID MOVE_UUID   = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-111111111111");
    private static final UUID ATKSPD_UUID = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-222222222222");
    private static final UUID ATKDAM_UUID = UUID.fromString("b2d2c2e0-2c9c-4b0c-9e8a-333333333333");

    public ElectroshockMobEffect() {
        super(MobEffectCategory.HARMFUL, 0x42C5FF); // color azul eléctrico
        // −50% Movimiento
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVE_UUID.toString(),
                -0.50D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // −25% Velocidad de ataque
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ATKSPD_UUID.toString(),
                -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // −33% Daño de ataque
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATKDAM_UUID.toString(),
                -0.33D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    // No necesitamos ticks de “poison/regen”, los eventos harán el resto
    @Override public boolean isDurationEffectTick(int duration, int amplifier) { return false; }

    // Ocultar icono/HUD del efecto (Forge client extension)
    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance inst) {
                return false;
            }

            public boolean isVisibleInHUD(MobEffectInstance inst) {
                return false;
            }
        });
    }
}
