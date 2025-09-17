package net.lalusko.createcogsinvaders;

import com.mojang.logging.LogUtils;
import net.lalusko.createcogsinvaders.block.ModBlocks;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.enchantment.ModEnchantments;
import net.lalusko.createcogsinvaders.entity.ModEntities;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.lalusko.createcogsinvaders.item.custom.TeslaCannonBEWLR;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(CreateCogsInvadersMod.MOD_ID)
public class CreateCogsInvadersMod {
    public static final String MOD_ID = "create_cogs_invaders";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCogsInvadersMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientInit {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ModEntities.ELECTROSHOCK_CHARGE.get(), ThrownItemRenderer::new);

            ItemProperties.register(ModItems.TESLA_CANNON.get(), new ResourceLocation("recoil"), (stack, level, entity, seed) -> {
                if (entity == null || level == null) return 0f;
                long last = stack.getOrCreateTag().getLong("lastShot");
                long dt = level.getGameTime() - last;
                // 0→1 decae en ~6 ticks: pico de retroceso y vuelve
                if (dt >= 0 && dt <= 6) return 1f - (dt / 6f);
                return 0f;
            });
        }
    }
}
