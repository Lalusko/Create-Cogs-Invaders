package net.lalusko.createcogsinvaders.block.custom;

import net.lalusko.createcogsinvaders.item.ModItems;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArmorRepairStationBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 4);
    public static final int CAPACITY = 4;

    private static final double NOZZLE_DY    = 2.0 / 16.0;
    private static final double NOZZLE_FRONT = 0.14994;

    private static final double THICK  = 8.0 / 16.0;
    private static final double MARGIN = 2.0 / 16.0;

    public ArmorRepairStationBlock() {
        super(Properties
                .copy(Blocks.IRON_BLOCK)
                .sound(SoundType.NETHERITE_BLOCK)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BLOCKSTATE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, BLOCKSTATE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter w, BlockPos p, CollisionContext c) {
        return switch (s.getValue(FACING)) {
            case SOUTH -> box(16*MARGIN, 0, 0,                16*(1-MARGIN), 16, 16*THICK);
            case NORTH -> box(16*MARGIN, 0, 16*(1-THICK),     16*(1-MARGIN), 16, 16);
            case EAST  -> box(0,             0, 16*MARGIN,    16*THICK,      16, 16*(1-MARGIN));
            case WEST  -> box(16*(1-THICK),  0, 16*MARGIN,    16,            16, 16*(1-MARGIN));
            default    -> box(0, 0, 0, 16, 16, 16);
        };
    }
    @Override public VoxelShape getCollisionShape(BlockState s, BlockGetter w, BlockPos p, CollisionContext c) {
        return getShape(s, w, p, c);
    }

    @Override public BlockState rotate(BlockState s, Rotation r) { return s.setValue(FACING, r.rotate(s.getValue(FACING))); }
    @Override public BlockState mirror(BlockState s, Mirror m) { return s.rotate(m.getRotation(s.getValue(FACING))); }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        int charges = state.getValue(BLOCKSTATE);
        ItemStack main = player.getMainHandItem();
        ItemStack off  = player.getOffhandItem();
        boolean hasMain = main.is(ModItems.XP_CONTAINER_FULL.get());
        boolean hasOff  = off.is(ModItems.XP_CONTAINER_FULL.get());
        boolean holdingContainer = hasMain || hasOff;

        if (holdingContainer) {
            if (charges >= CAPACITY) {
                playError(level, pos);
                return InteractionResult.CONSUME;
            }
            if (!player.getAbilities().instabuild) {
                if (hasMain) main.shrink(1); else off.shrink(1);
            }
            level.setBlock(pos, state.setValue(BLOCKSTATE, charges + 1), 3);
            level.playSound(null, pos, ModSounds.STATION_RELOAD.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.CONSUME;
        }

        if (main.isEmpty()) {
            if (!hasAnyRepairableArmor(player)) { playError(level, pos); return InteractionResult.CONSUME; }
            if (charges <= 0) { playError(level, pos); return InteractionResult.CONSUME; }

            repairWornArmor(player);

            level.setBlock(pos, state.setValue(BLOCKSTATE, charges - 1), 3);
            level.playSound(null, pos, ModSounds.STATION_USE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

            if (level instanceof ServerLevel srv) {
                spawnSoulFlameJet(srv, pos, state.getValue(FACING));
            }
            return InteractionResult.CONSUME;
        }

        playError(level, pos);
        return InteractionResult.CONSUME;
    }

    private void playError(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSounds.STATION_ERROR.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    private boolean hasAnyRepairableArmor(Player p) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            if (isRepairableArmorPiece(p.getItemBySlot(slot))) return true;
        }
        return false;
    }

    private void repairWornArmor(Player p) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack s = p.getItemBySlot(slot);
            if (isRepairableArmorPiece(s)) {
                s.setDamageValue(0);
            }
        }
    }

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private boolean isRepairableArmorPiece(ItemStack s) {
        if (s.isEmpty()) return false;
        if (!s.isDamageableItem()) return false;
        if (s.getDamageValue() <= 0) return false;
        if (s.hasTag() && s.getTag().getBoolean("Unbreakable")) return false;
        if (s.getMaxDamage() <= 0) return false;
        return true;
    }

    private void spawnSoulFlameJet(ServerLevel srv, BlockPos pos, Direction facing) {
        final double baseY = pos.getY() + NOZZLE_DY;
        double baseX = pos.getX() + 0.5;
        double baseZ = pos.getZ() + 0.5;

        double dirX = 0, dirZ = 0;
        switch (facing) {
            case SOUTH -> { baseZ += NOZZLE_FRONT; dirZ =  1; }
            case NORTH -> { baseZ -= NOZZLE_FRONT; dirZ = -1; }
            case EAST  -> { baseX += NOZZLE_FRONT; dirX =  1; }
            case WEST  -> { baseX -= NOZZLE_FRONT; dirX = -1; }
        }

        for (int i = 0; i < 6; i++) {
            double px = baseX + dirX * (i * 0.08);
            double pz = baseZ + dirZ * (i * 0.08);
            srv.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, baseY, pz,
                    1, 0.0, 0.0, 0.0, 0.02);
        }
    }
}
