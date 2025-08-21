package net.lalusko.createcogsinvaders;

import com.mojang.logging.LogUtils;
import net.lalusko.createcogsinvaders.block.ModBlocks;
import net.lalusko.createcogsinvaders.effect.ModEffects;
import net.lalusko.createcogsinvaders.enchantment.ModEnchantments;
import net.lalusko.createcogsinvaders.entity.ModEntities;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraftforge.api.distmarker.Dist;
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

        GeckoLib.initialize();

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

        }
    }
}
