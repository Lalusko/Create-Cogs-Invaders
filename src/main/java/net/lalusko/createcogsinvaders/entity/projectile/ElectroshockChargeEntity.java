package net.lalusko.createcogsinvaders.entity.projectile;

import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.entity.ModEntities;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ElectroshockChargeEntity extends ThrowableItemProjectile {

    public ElectroshockChargeEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ElectroshockChargeEntity(Level pLevel) {
        super(ModEntities.ELECTROSHOCK_CHARGE.get(), pLevel);
    }

    public ElectroshockChargeEntity(Level pLevel, LivingEntity livingEntity) {
        super(ModEntities.ELECTROSHOCK_CHARGE.get(), livingEntity, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ELECTROSHOCK_CHARGE.get();
    }

    @Override
    protected float getGravity() {
        // Trayectoria recta (sin caída)
        return 0.0f;
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide) {
            if (hit.getEntity() instanceof LivingEntity target) {
                // Condición: vida ACTUAL < 50.0f
                if (target.getHealth() < 50.0f) {
                    // 1 hp de daño (1.0f = 1 punto de vida = 0.5 corazón)
                    DamageSource src = damageSources().thrown(this, this.getOwner());
                    target.hurt(src, 1.0f);

                    // Aplica tu efecto de poción "Electroshock"
                    // Duración/Amplificador a tu gusto (ticks: 20 = 1s)
                    target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            ModEffects.ELECTROSHOCK.get(), 20 * 10, 0, false, true));
                }
            }
            this.discard(); // Destruye el proyectil tras impactar
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide) {
            this.discard();
        }
    }
}
