package net.lalusko.createcogsinvaders.enchantment;

import net.lalusko.createcogsinvaders.CreateCogsInvadersMod;
import net.lalusko.createcogsinvaders.item.custom.MedkitItem;
import net.lalusko.createcogsinvaders.item.custom.RepairKitItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CreateCogsInvadersMod.MOD_ID);

    public static final EnchantmentCategory REPAIR_KIT_CATEGORY =
            EnchantmentCategory.create("repair_kid_only", item -> item instanceof RepairKitItem);

    public static final RegistryObject<Enchantment> RESTORATION =
            ENCHANTMENTS.register("restoration",
                    () -> new net.lalusko.createcogsinvaders.enchantment.RestorationEnchantment(
                            Enchantment.Rarity.VERY_RARE, REPAIR_KIT_CATEGORY));

    public static final EnchantmentCategory MEDKIT_CATEGORY =
            EnchantmentCategory.create("medkit_only", item -> item instanceof MedkitItem);

    public static final RegistryObject<Enchantment> FIRST_AID =
            ENCHANTMENTS.register("first_aid",
                    () -> new net.lalusko.createcogsinvaders.enchantment.FirstAidEnchantment(
                            Enchantment.Rarity.VERY_RARE, MEDKIT_CATEGORY));
}
