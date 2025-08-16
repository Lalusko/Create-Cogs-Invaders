package net.lalusko.createcogsinvaders;

import net.lalusko.createcogsinvaders.block.ModBlocks;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateCogsInvadersMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CREATE_COGS_INVADERS = CREATIVE_MODE_TABS.register("create_cogs_invaders",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ADVANCED_CHIP.get()))
                    .title(Component.translatable("creativetab.create_cogs_invaders"))
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.SULFUR_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_SULFUR_ORE.get());
                        pOutput.accept(ModBlocks.RAW_SULFUR_BLOCK.get());
                        pOutput.accept(ModBlocks.HEALING_STATION.get());
                        pOutput.accept(ModBlocks.ARMOR_REPAIR_STATION.get());

                        pOutput.accept(ModItems.MEDKIT.get());
                        pOutput.accept(ModItems.REPAIR_KIT.get());
                        pOutput.accept(ModItems.RAW_SULFUR.get());
                        pOutput.accept(ModItems.POWDERED_SULFUR.get());
                        pOutput.accept(ModItems.COPPER_WIRE.get());
                        pOutput.accept(ModItems.ELECTROCONDUCTIVE_CHIP.get());
                        pOutput.accept(ModItems.DYNAMIC_CHIP.get());
                        pOutput.accept(ModItems.ADVANCED_CHIP.get());
                        pOutput.accept(ModItems.ELECTROCONDUCTIVE_MECHANISM.get());
                        pOutput.accept(ModItems.DYNAMIC_MECHANISM.get());
                        pOutput.accept(ModItems.ADVANCED_MECHANISM.get());
                        pOutput.accept(ModItems.ADVANCED_MECHANISM.get());
                        pOutput.accept(ModItems.LARGE_BOTTLE.get());
                        pOutput.accept(ModItems.XP_CONTAINER.get());
                        pOutput.accept(ModItems.TESLA_BATTERY_AMMO.get());
                        pOutput.accept(ModItems.DIAL.get());
                        pOutput.accept(ModItems.BRASS_MUSIC_DISC.get());
                    }))
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
