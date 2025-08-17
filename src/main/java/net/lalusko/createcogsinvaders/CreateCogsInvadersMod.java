package net.lalusko.createcogsinvaders;

import com.mojang.logging.LogUtils;
import net.lalusko.createcogsinvaders.block.ModBlocks;
import net.lalusko.createcogsinvaders.enchantment.ModEnchantments;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreateCogsInvadersMod.MOD_ID)
public class CreateCogsInvadersMod {
    public static final String MOD_ID = "create_cogs_invaders";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCogsInvadersMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModEnchantments.ENCHANTMENTS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModCreativeTabEvents {
        @SubscribeEvent
        public static void onBuildContents(net.minecraftforge.event.BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.FOOD_AND_DRINKS) {
                // 1) Vacía
                event.accept(ModItems.LARGE_BOTTLE.get());

                // 2) Ordenar: primero agua/awkward/thick/mundane, luego el resto
                var all = new java.util.ArrayList<>(net.minecraftforge.registries.ForgeRegistries.POTIONS.getValues());
                // Quita la “EMPTY” (no es una poción real)
                all.remove(net.minecraft.world.item.alchemy.Potions.EMPTY);

                // Helper para añadir una variante
                java.util.function.Consumer<net.minecraft.world.item.alchemy.Potion> add = p -> {
                    var stack = new net.minecraft.world.item.ItemStack(ModItems.LARGE_BOTTLE.get());
                    net.lalusko.createcogsinvaders.item.custom.LargeBottleItem(stack, 4); // 4 usos
                    net.minecraft.world.item.alchemy.PotionUtils.setPotion(stack, p);
                    event.accept(stack);
                };

                // Prioriza algunas como hace vanilla
                add.accept(net.minecraft.world.item.alchemy.Potions.WATER);
                add.accept(net.minecraft.world.item.alchemy.Potions.AWKWARD);
                add.accept(net.minecraft.world.item.alchemy.Potions.THICK);
                add.accept(net.minecraft.world.item.alchemy.Potions.MUNDANE);

                // 3) El resto, ordenados por id para estabilidad
                all.remove(net.minecraft.world.item.alchemy.Potions.WATER);
                all.remove(net.minecraft.world.item.alchemy.Potions.AWKWARD);
                all.remove(net.minecraft.world.item.alchemy.Potions.THICK);
                all.remove(net.minecraft.world.item.alchemy.Potions.MUNDANE);
                all.sort(java.util.Comparator.comparing(p -> net.minecraftforge.registries.ForgeRegistries.POTIONS.getKey(p)));
                for (var p : all) add.accept(p);
            }
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientInit {
        @SubscribeEvent
        public static void onRegisterItemColors(net.minecraftforge.client.event.RegisterColorHandlersEvent.Item e) {
            e.register((stack, tintIndex) -> {
                if (tintIndex == 1) {

                    return net.minecraft.world.item.alchemy.PotionUtils.getColor(stack);
                }
                return 0xFFFFFF;
            }, ModItems.LARGE_BOTTLE.get());
        }
    }
}
