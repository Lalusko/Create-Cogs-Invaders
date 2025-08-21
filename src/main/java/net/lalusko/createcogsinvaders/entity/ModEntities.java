package net.lalusko.createcogsinvaders.entity;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreateCogsInvadersMod.MOD_ID);

    public static final RegistryObject<EntityType<ElectroshockChargeEntity>> ELECTROSHOCK_CHARGE =
            ENTITY_TYPES.register("electroshock_charge", () ->
                    EntityType.Builder.<ElectroshockChargeEntity>of(ElectroshockChargeEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)     // hitbox pequeño tipo snowball
                            .clientTrackingRange(64) // buen rango de tracking
                            .updateInterval(10)
                            .build(new ResourceLocation("create_cogs_invaders", "electroshock_charge").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
