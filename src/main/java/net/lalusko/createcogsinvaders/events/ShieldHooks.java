package net.lalusko.createcogsinvaders.events;

import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShieldHooks {

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        LivingEntity defender = event.getEntity();
        Level level = defender.level();
        if (level.isClientSide) return;

        ItemStack using = defender.getUseItem();
        if (!using.is(ModItems.TESLA_SHIELD.get())) return;

        DamageSource source = event.getDamageSource();
        Entity direct = source.getDirectEntity();
        Entity attackerEnt = source.getEntity();

        LivingEntity attacker = null;
        if (direct instanceof LivingEntity le) {
            attacker = le;
        } else if (attackerEnt instanceof LivingEntity le) {
            attacker = le;
        }
        if (attacker == null) return;

        if (attacker.getHealth() >= 50.0f) return;

        attacker.addEffect(new MobEffectInstance(ModEffects.ELECTROSHOCK.get(), 100));

        // (Opcional) feedback: ligerísimo daño/knockback/sonido/partículas aquí si quieres.
        // attacker.hurt(attacker.damageSources().magic(), 0.0F);
    }
}
