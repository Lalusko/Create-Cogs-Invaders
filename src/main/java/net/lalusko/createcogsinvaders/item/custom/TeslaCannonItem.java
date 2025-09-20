package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeslaCannonItem extends Item {
    public TeslaCannonItem(Properties props) { super(props); }

    @Override
    public net.minecraft.world.item.UseAnim getUseAnimation(net.minecraft.world.item.ItemStack stack) {
        return net.minecraft.world.item.UseAnim.BOW;
    }

    @Override
    public int getUseDuration(net.minecraft.world.item.ItemStack stack) {
        return 72000; // mantener uso
    }

    @Override
    public net.minecraft.world.InteractionResultHolder<net.minecraft.world.item.ItemStack> use(net.minecraft.world.level.Level level, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
        net.minecraft.world.item.ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand); // fuerza pose de apuntado
        // Disparo inmediato (o mueve la lógica a releaseUsing si prefieres al soltar)
        if (!level.isClientSide) {
            // TODO: spawn del proyectil + sonido
            player.getCooldowns().addCooldown(this, 12);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
        return net.minecraft.world.InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    // Sólo si usas la Opción B (disparo al soltar)
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) return;
        if (!level.isClientSide) {
            // ... tu lógica de proyectil ...
            player.getCooldowns().addCooldown(this, 12);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
            level.playSound(null, player.blockPosition(), ModSounds.TESLA_CANNON_SHOOT.get(), SoundSource.PLAYERS, 0.6f, 1.6f);
        }
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
            @Override
            public com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer getCustomRenderer() {
                return new net.lalusko.createcogsinvaders.client.render.TeslaCannonRenderer();
            }
        });
    }
}
