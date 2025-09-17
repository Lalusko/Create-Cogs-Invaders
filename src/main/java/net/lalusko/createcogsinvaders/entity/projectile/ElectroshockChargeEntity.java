package net.lalusko.createcogsinvaders.entity.projectile;

import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.entity.ModEntities;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class ElectroshockChargeEntity extends ThrowableItemProjectile {

    private int life;

    public ElectroshockChargeEntity(EntityType<? extends ElectroshockChargeEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public ElectroshockChargeEntity(Level level, LivingEntity shooter) {
        super(ModEntities.ELECTROSHOCK_CHARGE.get(), shooter, level);
        this.noCulling = true;
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ELECTROSHOCK_CHARGE.get();
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (++life > 200) this.discard();

        if (!level().isClientSide) {
            var aabb = this.getBoundingBox().inflate(0.05);
            for (var e : level().getEntities(this, aabb, ent -> ent instanceof LivingEntity)) {
                LivingEntity living = (LivingEntity) e;
                if (living.getHealth() < 50.0f) {
                    living.addEffect(new MobEffectInstance(ModEffects.ELECTROSHOCK.get(), 100, 0));
                }
                this.discard();
                break;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide && result.getEntity() instanceof LivingEntity living) {

            if (living.getHealth() < 50.0f) {
                living.addEffect(new MobEffectInstance(ModEffects.ELECTROSHOCK.get(), 100, 0)); // 5s, ampl. 0
            }

            living.hurt(level().damageSources().indirectMagic(this, this.getOwner()), 0.1F);

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (!level().isClientSide) {
            if (player.getHealth() < 50.0f) {
                player.addEffect(new MobEffectInstance(ModEffects.ELECTROSHOCK.get(), 100, 0));
            }
            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getItemRaw() {
        return new ItemStack(ModItems.ELECTROSHOCK_CHARGE.get());
    }
}
