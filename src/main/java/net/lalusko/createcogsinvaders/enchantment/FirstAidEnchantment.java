package net.lalusko.createcogsinvaders.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FirstAidEnchantment extends Enchantment {
    public FirstAidEnchantment(Rarity rarity, EnchantmentCategory cat) {
        super(rarity, cat, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }
    @Override public int getMaxLevel() { return 3; }
    @Override public boolean isTreasureOnly() { return true; }
    @Override public boolean isDiscoverable() { return true; }
    @Override public boolean isTradeable() { return true; }
}
