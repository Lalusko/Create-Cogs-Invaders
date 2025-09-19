package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.lalusko.createcogsinvaders.item.client.render.TeslaCannonBEWLR;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class TeslaCannonItem extends Item {
    public TeslaCannonItem(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ElectroshockChargeEntity proj = new ElectroshockChargeEntity(level, player);
            proj.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

            Vec3 look = player.getLookAngle();
            float speed = 2.4f;
            proj.shoot(look.x, look.y, look.z, speed, 0.0f);

            level.addFreshEntity(proj);

            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            player.getCooldowns().addCooldown(this, 12);
            level.playSound(null, player.blockPosition(), ModSounds.TESLA_CANNON_SHOOT.get(), SoundSource.PLAYERS, 0.6f, 1.0f);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new TeslaCannonBEWLR(
                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                        Minecraft.getInstance().getEntityModels()
                );
            }
        });
    }
}
