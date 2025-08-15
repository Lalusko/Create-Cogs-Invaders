package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DialItem extends Item {
    public static final String KEY_MODE = "Mode";

    public DialItem(Properties p) { super(p.stacksTo(1)); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack s = player.getItemInHand(hand);
        if (!level.isClientSide) {
            int m = s.getOrCreateTag().getInt(KEY_MODE);
            m = (m + 1) % 3;
            s.getOrCreateTag().putInt(KEY_MODE, m);
            s.getOrCreateTag().putInt("CustomModelData", m);
        }
        return InteractionResultHolder.sidedSuccess(s, level.isClientSide);
    }

    @Override
    public Component getName(ItemStack stack) {
        int m = stack.getOrCreateTag().getInt(KEY_MODE);
        return switch (m) {
            case 1 -> Component.translatable("item.create_cogs_invaders.dial_healing");
            case 2 -> Component.translatable("item.create_cogs_invaders.dial_repair");
            default -> Component.translatable("item.create_cogs_invaders.dial");
        };
    }
}
