package net.lalusko.createcogsinvaders.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import java.util.List;

public class LargeBottleItem extends Item {

    private static final String TAG_USES = "Uses";   // 0..4
    // Nota: el TAG "Potion" es el mismo que usa vanilla para Items.POTION

    private static final int MAX_USES = 4;
    private static final int COOLDOWN_TICKS = 60;

    public LargeBottleItem(Properties props) {
        super(props.stacksTo(1));
    }

    // =============== NBT helpers ===============
    public static int getUses(ItemStack stack) {
        if (!stack.hasTag()) return 0;
        return Math.max(0, Math.min(MAX_USES, stack.getTag().getInt(TAG_USES)));
    }
    public static void setUses(ItemStack stack, int uses) {
        stack.getOrCreateTag().putInt(TAG_USES, Math.max(0, Math.min(MAX_USES, uses)));
    }
    public static void clearContents(ItemStack stack) {
        // Deja la botella "vacía": sin Potion y sin Uses
        if (!stack.hasTag()) return;
        stack.getTag().remove(TAG_USES);
        stack.getTag().remove("Potion");
        if (stack.getTag().isEmpty()) stack.setTag(null);
    }
    private static boolean hasPotion(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Potion");
    }

    // =============== Uso sobre uno mismo ===============
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bottle = player.getItemInHand(hand);

        if (!hasPotion(bottle) || getUses(bottle) <= 0) {
            return InteractionResultHolder.pass(bottle);
        }

        // Aplica a ti mismo
        boolean applied = applyBottleEffects(level, player, player, bottle);
        if (!applied) return InteractionResultHolder.pass(bottle);

        // Consumir uso, dejar vacía si llega a 0
        consumeOneUse(level, player, hand, bottle);

        return InteractionResultHolder.sidedSuccess(bottle, level.isClientSide);
    }

    // =============== Uso sobre otra entidad ===============
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.level();
        if (!hasPotion(stack) || getUses(stack) <= 0) {
            return InteractionResult.PASS;
        }

        boolean applied = applyBottleEffects(level, player, target, stack);
        if (!applied) return InteractionResult.PASS;

        consumeOneUse(level, player, hand, stack);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // =============== Lógica de efectos ===============
    private boolean applyBottleEffects(Level level, Player user, LivingEntity target, ItemStack bottle) {
        // Leer el "Potion" como lo hace vanilla
        // (funciona aunque el item no sea Items.POTION)
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(bottle);

        // Si es agua, no hay efectos (vanilla "minecraft:water" no aplica nada)
        if (effects.isEmpty()) {
            // puedes agregar lógica opcional (apagar fuego al target, etc.)
            playDrink(level, user);
            user.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            return true; // cuenta como uso consumido aunque sea agua (cámbialo si no quieres)
        }

        if (!level.isClientSide) {
            for (MobEffectInstance e : effects) {
                // Copia para no mutar el original
                target.addEffect(new MobEffectInstance(e));
            }
        }
        playDrink(level, user);
        user.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        return true;
    }

    private void playDrink(Level level, Player user) {
        level.playSound(null, user.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.9f, 1.0f);
    }

    private void consumeOneUse(Level level, Player player, InteractionHand hand, ItemStack bottle) {
        if (!level.isClientSide) {
            if (player.getAbilities().instabuild) {
                // creativo: no gasta usos
                return;
            }
            int uses = getUses(bottle);
            if (uses > 1) {
                setUses(bottle, uses - 1);
            } else {
                clearContents(bottle); // vuelve a "vacía"
            }
        }
    }

    // =============== Utilidades públicas ===============
    /** Rellena la botella con una poción vanilla y usos=4 (útil en recetas/datapacks). */
    public static ItemStack withPotion(ItemStack emptyBottle, Level level, ResourceLocation potionId) {
        ItemStack out = emptyBottle.copy();
        Potion potion = level.registryAccess().registryOrThrow(Registries.POTION).get(potionId);
        if (potion != null) {
            PotionUtils.setPotion(out, potion);
            setUses(out, MAX_USES);
        }
        return out;
    }
}
