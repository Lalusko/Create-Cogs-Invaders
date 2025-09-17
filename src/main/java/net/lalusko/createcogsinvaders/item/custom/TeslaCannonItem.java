package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class TeslaCannonItem extends Item {
    private static final int COOLDOWN_TICKS = 20; // 1s

    public TeslaCannonItem(Properties props) { super(props); }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final TeslaCannonBEWLR renderer = new TeslaCannonBEWLR();
            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Spawnea tu ElectroshockChargeEntity recto
            var proj = new ElectroshockChargeEntity(level, player);
            proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2.4f, 0f);
            level.addFreshEntity(proj);

            // Marca tiempo de “disparo” para animar recoil en cliente
            stack.getOrCreateTag().putLong("lastShot", player.level().getGameTime());

            if (level.isClientSide()) {
                // espejo en cliente (evita delay visual si hay lag)
                stack.getOrCreateTag().putLong("lastShot", net.minecraft.client.Minecraft.getInstance().level.getGameTime());
            }

            // Cooldown vanilla
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

            // Durabilidad/energía si quieres
            if (!player.getAbilities().instabuild) stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }

        // Sonido local
        level.playSound(player, player.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.7f, 1.2f);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
