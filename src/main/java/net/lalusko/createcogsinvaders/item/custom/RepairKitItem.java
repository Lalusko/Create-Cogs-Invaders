package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.enchantment.ModEnchantments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class RepairKitItem extends Item {
    private static final int BASE_REPAIR_PER_USE = 50;
    private static final int COOLDOWN_TICKS = 100;

    public RepairKitItem(Properties props) {
        super(props.stacksTo(1).durability(5));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack kit = player.getItemInHand(hand);
        ItemStack target = (hand == InteractionHand.MAIN_HAND) ? player.getOffhandItem() : player.getMainHandItem();

        if (target.isEmpty() || !target.isDamageableItem()) return InteractionResultHolder.pass(kit);
        if (target.getItem() == this) return InteractionResultHolder.pass(kit);
        int dmg = target.getDamageValue();
        if (dmg <= 0) return InteractionResultHolder.pass(kit);

        if (!level.isClientSide) {
            int restLvl = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RESTORATION.get(), kit);
            int amount  = BASE_REPAIR_PER_USE + restLvl * 30;
            int fix     = Math.min(amount, dmg);

            target.setDamageValue(dmg - fix);
            kit.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            level.playSound(null, player.blockPosition(),
                    SoundEvents.SMITHING_TABLE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return InteractionResultHolder.sidedSuccess(kit, level.isClientSide);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment ench) {
        if (ench == Enchantments.MENDING || ench == Enchantments.UNBREAKING) return false;
        return super.canApplyAtEnchantingTable(stack, ench);
    }
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        var map = EnchantmentHelper.getEnchantments(book);
        if (map.containsKey(Enchantments.MENDING) || map.containsKey(Enchantments.UNBREAKING)) return false;
        return super.isBookEnchantable(stack, book);
    }
}