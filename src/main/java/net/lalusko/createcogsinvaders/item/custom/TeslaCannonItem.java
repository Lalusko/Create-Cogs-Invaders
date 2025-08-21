package net.lalusko.createcogsinvaders.item.custom;

import net.lalusko.createcogsinvaders.entity.projectile.ElectroshockChargeEntity;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class TeslaCannonItem extends Item implements GeoItem {
    private static final String NBT_AMMO = "Ammo";
    private static final int MAX_AMMO = 16;
    private static final int DURABILITY = 100;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TeslaCannonItem(Properties props) {
        super(props.durability(DURABILITY));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    /* ------------ Ammo lifecycle: start at 0/16 ------------ */
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt(NBT_AMMO, 0);
        return stack;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putInt(NBT_AMMO, 0);
    }

    /* ----------------- GeckoLib animations ----------------- */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.setAnimation(RawAnimation.begin().thenLoop("Idle"));
            return PlayState.CONTINUE;
        }));
    }

    public void playShootAnimation(Player player) {
        // Triggers the "On" animation for ~1s (as defined in your animation file)
        GeoItem.triggerAnim(player, this, "controller", "On");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /* --------------------- Firing logic -------------------- */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int ammo = stack.getOrCreateTag().getInt(NBT_AMMO);

        if (!level.isClientSide) {
            if (ammo > 0) {
                // Spawn projectile
                ElectroshockChargeEntity proj = new ElectroshockChargeEntity(level, player);
                proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 0.0F);
                level.addFreshEntity(proj);

                // Sounds
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        ModSounds.TESLA_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

                // Consume ammo & durability
                stack.getTag().putInt(NBT_AMMO, ammo - 1);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));

                // Animation
                playShootAnimation(player);

                return InteractionResultHolder.sidedSuccess(stack, false);
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    /* ----------------------- Reload ------------------------ */
    public static boolean tryReload(Player player, ItemStack cannon, ItemStack battery, Level level) {
        if (battery.isEmpty()) return false;
        int ammo = cannon.getOrCreateTag().getInt(NBT_AMMO);
        if (ammo >= MAX_AMMO) return false; // already full

        cannon.getTag().putInt(NBT_AMMO, MAX_AMMO);
        battery.shrink(1);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.AMMO_RELOAD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        return true;
    }

    /* -------------------- Tooltip / UI --------------------- */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int ammo = stack.getOrCreateTag().getInt(NBT_AMMO);
        tooltip.add(Component.literal("Energy: " + ammo + "/" + MAX_AMMO)
                .withStyle(ammo > 0 ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));
        int left = stack.getMaxDamage() - stack.getDamageValue();
        tooltip.add(Component.literal("Wear: " + left + "/" + stack.getMaxDamage())
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    /* -------------- Provide GeckoLib renderer -------------- */
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoItemRenderer<?> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    // Replace TeslaCannonModel with your actual GeoModel class for this item
                    renderer = new GeoItemRenderer<>(new net.lalusko.createcogsinvaders.client.model.item.TeslaCannonModel());
                }
                return renderer;
            }
        });
    }

    /* --------- Optional: show ammo as item bar --------- */
//    @Override
//    public boolean isBarVisible(ItemStack stack) {
//        return true;
//    }
//
//    @Override
//    public int getBarWidth(ItemStack stack) {
//        int ammo = stack.getOrCreateTag().getInt(NBT_AMMO);
//        return Math.round((ammo / (float) MAX_AMMO) * 13.0f);
//    }
//
//    @Override
//    public int getBarColor(ItemStack stack) {
//        return 0x00C8FF;
//    }
}
