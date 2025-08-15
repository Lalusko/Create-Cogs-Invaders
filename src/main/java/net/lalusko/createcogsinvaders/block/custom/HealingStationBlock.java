package net.lalusko.createcogsinvaders.block.custom;

import net.lalusko.createcogsinvaders.item.ModItems;
import net.lalusko.createcogsinvaders.particle.ModParticles;
import net.lalusko.createcogsinvaders.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

public class HealingStationBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 4);
    public static final int CAPACITY = 4;

    // Nozzle (medido de tu modelo): centrado, un poco hacia el frente y 2px arriba
    private static final double NOZZLE_DY    = 2.0 / 16.0;   // 0.125
    private static final double NOZZLE_FRONT = 0.14994;      // (10.399-8)/16 aprox

    // Grosor del “slab vertical”: 6px (0.375 bloques). Anclado AL FRENTE (hacia FACING)
    private static final double THICK = 6.0 / 16.0;

    public HealingStationBlock() {
        super(Properties
                .copy(Blocks.IRON_BLOCK)                 // dureza/resistencia como hierro
                .sound(SoundType.NETHERITE_BLOCK)        // sonido de netherita
                .noOcclusion());                         // modelo delgado/huecos
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BLOCKSTATE, 0));
    }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, BLOCKSTATE);
    }

    @Override public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Como horno: la cara mira hacia el jugador
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override public VoxelShape getShape(BlockState s, BlockGetter w, BlockPos p, CollisionContext c) {
        Direction f = s.getValue(FACING);
        // cajas ancladas al lado "frente" según FACING
        return switch (f) {
            case NORTH -> box(0, 0, 0,     16, 16, 16 * THICK);              // z: 0 .. 0.375
            case SOUTH -> box(0, 0, 16*(1-THICK), 16, 16, 16);               // z: 0.625 .. 1
            case WEST  -> box(0, 0, 0,     16*THICK, 16, 16);                // x: 0 .. 0.375
            case EAST  -> box(16*(1-THICK), 0, 0, 16, 16, 16);               // x: 0.625 .. 1
            default    -> box(0, 0, 0, 16, 16, 16);
        };
    }

    // Si también quieres colisión igual que visual:
    @Override public VoxelShape getCollisionShape(BlockState s, BlockGetter w, BlockPos p, CollisionContext c) {
        return getShape(s, w, p, c);
    }

    @Override public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @Override public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

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

        // Recargar (no partículas; solo sonido custom)
        if (holdingContainer) {
            if (charges >= CAPACITY) {
                playError(level, pos);
                return InteractionResult.CONSUME;
            }
            if (hasMain) main.shrink(1); else off.shrink(1);
            level.setBlock(pos, state.setValue(BLOCKSTATE, charges + 1), 3);

            // SONIDO custom de recarga
            level.playSound(null, pos, ModSounds.STATION_RELOAD.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.CONSUME;
        }

        // Usar (mano principal vacía): cura total si hay carga y estás herido
        if (main.isEmpty()) {
            if (player.getHealth() >= player.getMaxHealth()) { playError(level, pos); return InteractionResult.CONSUME; }
            if (charges <= 0) { playError(level, pos); return InteractionResult.CONSUME; }

            player.setHealth(player.getMaxHealth());
            level.setBlock(pos, state.setValue(BLOCKSTATE, charges - 1), 3);

            // SONIDO custom de uso
            level.playSound(null, pos, ModSounds.STATION_USE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

            // PARTÍCULAS custom: SOLO aquí (tipo dispensador desde el nozzle)
            spawnNozzleJet(level, pos, state.getValue(FACING),
                    ModParticles.HEALING_PARTICLES.get(), // tu partícula custom
                    12,                          // cuántas
                    0.10);                       // “velocidad”/spread
            return InteractionResult.CONSUME;
        }

        // Nada de error con partículas: SOLO sonido
        playError(level, pos);
        return InteractionResult.CONSUME;
    }

    private void playError(Level level, BlockPos pos) {
        level.playSound(null, pos, ModSounds.STATION_ERROR.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    /**
     * Emite partículas desde la boquilla hacia el frente.
     * Si quieres más control direccional, puedes spawnear una a una con pequeñas variaciones.
     */
    private void spawnNozzleJet(Level level, BlockPos pos, Direction facing,
                                ParticleOptions particle, int count, double spread) {
        double xOff = 0, zOff = 0;
        switch (facing) {
            case SOUTH -> { xOff =  0.0;           zOff =  NOZZLE_FRONT; }
            case NORTH -> { xOff =  0.0;           zOff = -NOZZLE_FRONT; }
            case EAST  -> { xOff =  NOZZLE_FRONT;  zOff =  0.0; }
            case WEST  -> { xOff = -NOZZLE_FRONT;  zOff =  0.0; }
        }
        double x = pos.getX() + 0.5 + xOff;
        double y = pos.getY() + NOZZLE_DY;
        double z = pos.getZ() + 0.5 + zOff;

        if (level instanceof ServerLevel srv) {
            // spread pequeño para que parezca chorro; “speed” ajusta el movimiento según el tipo de partícula
            srv.sendParticles(particle, x, y, z, count, 0.02, 0.02, 0.02, spread);
        }
    }
}
