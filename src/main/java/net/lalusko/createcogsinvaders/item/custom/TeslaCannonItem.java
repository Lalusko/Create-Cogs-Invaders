package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class TeslaCannonItem extends Item {
    private static final int COOLDOWN_TICKS = 20;

    public TeslaCannonItem(Properties props) { super(props); }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new TeslaCannonBEWLR();
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() { return renderer; }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        long time = level.getGameTime();
        stack.getOrCreateTag().putLong("lastShot", time);
        if (level.isClientSide()) {
            stack.getOrCreateTag().putLong("lastShot", time);
        }

        if (!level.isClientSide) {
            ElectroshockChargeEntity proj = new ElectroshockChargeEntity(level, player);
            proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2.4f, 0f);
            level.addFreshEntity(proj);
        }

        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

        if (!player.getAbilities().instabuild) stack.hurtAndBreak(16, player, p -> p.broadcastBreakEvent(hand));

        level.playSound(player, player.blockPosition(),
                ModSounds.TESLA_CANNON_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1.0f);

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.NONE; }
}
