package net.lalusko.createcogsinvaders.effect;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.effect.custom.ElectroshockMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CreateCogsInvadersMod.MOD_ID);

    public static final RegistryObject<MobEffect> ELECTROSHOCK =
            EFFECTS.register("electroshock", ElectroshockMobEffect::new);
}
