package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.enchantment.ModEnchantments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class MedkitItem extends Item {

    private static final int BASE_HEAL_HP    = 6;
    private static final int HEAL_PER_LEVEL  = 2;
    private static final int COOLDOWN_TICKS  = 60;

    public MedkitItem(Properties props) {
        super(props.stacksTo(1).durability(5));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack medkit = player.getItemInHand(hand);

        if (!level.isClientSide) {
            boolean healed = tryHeal(level, player, player, medkit, hand);
            if (!healed) return InteractionResultHolder.pass(medkit);
        }
        return InteractionResultHolder.sidedSuccess(medkit, level.isClientSide);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        if (!level.isClientSide) {
            boolean healed = tryHeal(level, player, target, stack, hand);
            if (!healed) return InteractionResult.PASS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean tryHeal(Level level, Player user, LivingEntity target, ItemStack medkit, InteractionHand hand) {
        if (target.getHealth() >= target.getMaxHealth()) return false;
        int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FIRST_AID.get(), medkit);
        int amountHp  = BASE_HEAL_HP + enchLevel * HEAL_PER_LEVEL;

        target.heal(amountHp);

        if (!user.getAbilities().instabuild) medkit.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(hand));

        user.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        level.playSound(null, user.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 0.9f, 1.0f);

        return true;
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
