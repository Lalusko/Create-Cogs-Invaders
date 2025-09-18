package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TeslaCannonItem extends GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TeslaCannonItem(Item.Properties props) {
        super(props);
    }

    // --- Disparo + trigger de animación ---
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Marca de tiempo para "On"
        long t = level.getGameTime();
        stack.getOrCreateTag().putLong("lastShot", t);

        // Spawnea proyectil
        if (!level.isClientSide) {
            ElectroshockChargeEntity proj = new ElectroshockChargeEntity(level, player);
            proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2.4f, 0f);
            level.addFreshEntity(proj);
        }

        // Sonido
        level.playSound(player, player.blockPosition(),
                ModSounds.TESLA_CANNON_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 1.0f);

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.NONE; }

    // --- GeckoLib: Animaciones (nombres EXACTOS de tu .animation.json: "Idle" y "On") ---
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("Idle");
    private static final RawAnimation ON   = RawAnimation.begin().then("On", Animation.LoopType.PLAY_ONCE);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> {
            ItemStack s = state.getData(DataTickets.ITEMSTACK);
            long last = s.getOrCreateTag().getLong("lastShot");

            long gt = 0L;
            if (net.minecraft.client.Minecraft.getInstance().level != null)
                gt = net.minecraft.client.Minecraft.getInstance().level.getGameTime();

            // "On" dura ~1s (20 ticks) en tu anim json
            if (gt - last >= 0 && gt - last < 20) {
                state.setAndContinue(ON);
            } else {
                state.setAndContinue(IDLE);
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}
