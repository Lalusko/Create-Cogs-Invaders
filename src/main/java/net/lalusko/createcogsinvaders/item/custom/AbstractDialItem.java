package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class AbstractDialItem extends Item {
    private final Supplier<Item> next;
    private final Supplier<Item> prev;

    public AbstractDialItem(Properties props, Supplier<Item> next, Supplier<Item> prev) {
        super(props);
        this.next = next;
        this.prev = prev;
    }

    private void cycle(Level level, Player player, InteractionHand hand, boolean backwards) {
        ItemStack current = player.getItemInHand(hand);
        Item target = (backwards ? prev : next).get();
        ItemStack nextStack = new ItemStack(target, current.getCount());

        if (current.hasTag()) nextStack.setTag(current.getTag().copy());
        if (current.isDamageableItem() && nextStack.isDamageableItem()) {
            nextStack.setDamageValue(Math.min(current.getDamageValue(), nextStack.getMaxDamage()-1));
        }

        player.setItemInHand(hand, nextStack);
        player.awardStat(Stats.ITEM_USED.get(this));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.6f, 1.2f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            boolean backwards = player.isShiftKeyDown();
            cycle(level, player, hand, backwards);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide && ctx.getPlayer() != null) {
            boolean backwards = ctx.getPlayer().isShiftKeyDown();
            cycle(ctx.getLevel(), ctx.getPlayer(), ctx.getHand(), backwards);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
    }
}
