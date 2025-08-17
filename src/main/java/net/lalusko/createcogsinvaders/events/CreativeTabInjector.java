package net.lalusko.createcogsinvaders.events;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.item.ModItems;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

@Mod.EventBusSubscriber(modid = CreateCogsInvadersMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CreativeTabInjector {

    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        // Elige el tab vanilla donde quieres mostrar las variantes (ej. FOOD_AND_DRINKS)
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {

            // Botella vacía
            event.accept(ModItems.LARGE_BOTTLE.get());

            // Todas las variantes de poción (mismo ítem, distinto NBT)
            for (Potion p : ForgeRegistries.POTIONS.getValues()) {
                if (p == Potions.EMPTY) continue;

                ItemStack s = new ItemStack(ModItems.LARGE_BOTTLE.get());
                // 4 usos
                s.getOrCreateTag().putInt("Uses", 4);
                // NBT de la poción (vanilla)
                PotionUtils.setPotion(s, p);
                // Cambiar al modelo "filled" (si usas el override con CustomModelData)
                s.getOrCreateTag().putInt("CustomModelData", 1);

                event.accept(s);
            }
        }
    }
}
