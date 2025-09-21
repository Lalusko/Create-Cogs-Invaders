package net.lalusko.createcogsinvaders.effect.common;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElectroshockCommonEvents {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed e) {
        Player p = e.getEntity();
        if (p.hasEffect(ModEffects.ELECTROSHOCK.get())) {
            e.setNewSpeed(e.getNewSpeed() * 0.75f);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent e) {
        LivingEntity le = e.getEntity();
        if (!le.level().isClientSide && le.hasEffect(ModEffects.ELECTROSHOCK.get())) {
            if (le.hasEffect(MobEffects.LEVITATION)) return;

            Vec3 v = le.getDeltaMovement();
            if (le.isInWaterOrBubble()) {
                double ny = v.y > 0 ? v.y * 0.75 : v.y - 0.005;
                le.setDeltaMovement(v.x, ny, v.z);
                le.resetFallDistance();
            } else if (!le.onGround()) {
                le.setDeltaMovement(v.x, v.y - 0.03, v.z);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent e) {
        LivingEntity le = e.getEntity();
        if (le.hasEffect(ModEffects.ELECTROSHOCK.get()) && !le.hasEffect(MobEffects.LEVITATION)) {
            Vec3 v = le.getDeltaMovement();
            le.setDeltaMovement(v.x, v.y * 0.75, v.z);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added e) {
        if (e.getEffectInstance().getEffect() == ModEffects.ELECTROSHOCK.get()) {
            var lvl = e.getEntity().level();
            var pos = e.getEntity().blockPosition();
            lvl.playSound(null, pos, ModSounds.ELECTROSHOCK_START.get(),
                    SoundSource.PLAYERS, 0.8f, 1.1f);
        }
    }
}
