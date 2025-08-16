package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RepairKitItem extends Item {
    private static final String TAG_USES = "Uses";
    private static final int MAX_USES = 4;
    private static final int REPAIR_PER_USE = 50;
    private static final int COOLDOWN_TICKS = 100;
    private static final int LEVELS_PER_CHARGE = 1;

    public RepairKitItem(Properties props) {
        super(props.stacksTo(1));
    }

    public static int getUses(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG_USES)) return 0;
        int v = stack.getTag().getInt(TAG_USES);
        return Math.max(0, Math.min(MAX_USES, v));
    }

    public static void setUses(ItemStack stack, int value) {
        int v = Math.max(0, Math.min(MAX_USES, value));
        stack.getOrCreateTag().putInt(TAG_USES, v);
        syncCustomModelData(stack);
    }

    private static void syncCustomModelData(ItemStack stack) {
        int uses = getUses(stack);
        var tag = stack.getOrCreateTag();
        var display = tag.getCompound("display");
        if (uses <= 0) {
            display.remove("CustomModelData");
            if (display.isEmpty()) tag.remove("display"); else tag.put("display", display);
        } else {
            display.putInt("CustomModelData", uses); // 1..4
            tag.put("display", display);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack kit = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            int uses = getUses(kit);
            if (uses < MAX_USES) {
                if (!level.isClientSide) {
                    if (player.experienceLevel >= LEVELS_PER_CHARGE) {
                        player.giveExperienceLevels(-LEVELS_PER_CHARGE);
                        setUses(kit, uses + 1);
                        level.playSound(null, player.blockPosition(),
                                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                }
                return InteractionResultHolder.sidedSuccess(kit, level.isClientSide);
            }
            return InteractionResultHolder.sidedSuccess(kit, level.isClientSide);
        }

        ItemStack target = (hand == InteractionHand.MAIN_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        if (target.isEmpty() || !target.isDamageableItem()) {
            return InteractionResultHolder.pass(kit);
        }

        int uses = getUses(kit);
        if (uses <= 0) return InteractionResultHolder.pass(kit);

        int dmg = target.getDamageValue();
        if (dmg <= 0) return InteractionResultHolder.pass(kit);

        if (!level.isClientSide) {
            int fix = Math.min(REPAIR_PER_USE, dmg);
            target.setDamageValue(dmg - fix);
            setUses(kit, uses - 1);

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            level.playSound(null, player.blockPosition(),
                    SoundEvents.SMITHING_TABLE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return InteractionResultHolder.sidedSuccess(kit, level.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        int uses = getUses(stack);
        int cmd = stack.getOrCreateTag().getCompound("display").getInt("CustomModelData");
        if ((uses == 0 && cmd != 0) || (uses > 0 && cmd != uses)) {
            syncCustomModelData(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.create_cogs_invaders.repair_kit.uses", getUses(stack), MAX_USES));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}