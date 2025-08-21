package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeslaBatteryItem extends Item {
    public TeslaBatteryItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack ammoStack = player.getItemInHand(hand);
        ItemStack other = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (other.getItem() instanceof TeslaCannonItem) {
            boolean success = TeslaCannonItem.tryReload(player, other, ammoStack, level);
            if (success) {
                return InteractionResultHolder.sidedSuccess(ammoStack, level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }
}
