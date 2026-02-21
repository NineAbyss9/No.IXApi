
package com.bilibili.player_ix.noixmod_api.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class TeleportPlate
extends Block {
    public static final BooleanProperty UP;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final Map<BlockState, VoxelShape> shapesCache;
    public TeleportPlate() {
        super(Properties.of().sound(SoundType.GLASS).mapColor(DyeColor.YELLOW)
                .instrument(NoteBlockInstrument.BASEDRUM).strength(114514f, 114514f)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(NORTH, false)
                .setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream()
                .collect(Collectors.toMap(Function.identity(), TeleportPlate::calculateShape)));
    }

    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return this.shapesCache.get(p_60555_);
    }

    public boolean propagatesSkylightDown(BlockState p_181239_, BlockGetter p_181240_, BlockPos p_181241_) {
        return true;
    }

    public boolean canSurvive(BlockState p_57861_, LevelReader p_57862_, BlockPos p_57863_) {
        return this.hasFaces(this.getUpdatedState(p_57861_, p_57862_, p_57863_));
    }

    private boolean hasFaces(BlockState p_57908_) {
        return this.countFaces(p_57908_) > 0;
    }

    private int countFaces(BlockState p_57910_) {
        int i = 0;
        for (BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
            if (p_57910_.getValue(booleanproperty)) {
                ++i;
            }
        }
        return i;
    }

    private BlockState getUpdatedState(BlockState p_57902_, BlockGetter p_57903_, BlockPos p_57904_) {
        BlockPos blockpos = p_57904_.above();
        if (p_57902_.getValue(UP)) {
            p_57902_ = p_57902_.setValue(UP, isAcceptableNeighbour(p_57903_, blockpos, Direction.DOWN));
        }
        BlockState blockstate = null;
        Iterator<Direction> var6 = Direction.Plane.HORIZONTAL.iterator();
        while (true) {
            Direction direction;
            BooleanProperty booleanproperty;
            do {
                if (!var6.hasNext()) {
                    return p_57902_;
                }
                direction = var6.next();
                booleanproperty = VineBlock.getPropertyForFace(direction);
            } while(!p_57902_.getValue(booleanproperty));
            boolean flag = this.canSupportAtFace(p_57903_, p_57904_, direction);
            if (!flag) {
                if (blockstate == null) {
                    blockstate = p_57903_.getBlockState(blockpos);
                }
                flag = blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
            p_57902_ = p_57902_.setValue(booleanproperty, flag);
        }
    }

    private boolean canSupportAtFace(BlockGetter p_57888_, BlockPos p_57889_, Direction p_57890_) {
        if (p_57890_ == Direction.DOWN) {
            return false;
        } else {
            BlockPos blockpos = p_57889_.relative(p_57890_);
            if (isAcceptableNeighbour(p_57888_, blockpos, p_57890_)) {
                return true;
            } else if (p_57890_.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(p_57890_);
                BlockState blockstate = p_57888_.getBlockState(p_57889_.above());
                return blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
        }
    }

    public static boolean isAcceptableNeighbour(BlockGetter p_57854_, BlockPos p_57855_, Direction p_57856_) {
        return MultifaceBlock.canAttachTo(p_57854_, p_57856_, p_57855_, p_57854_.getBlockState(p_57855_));
    }

    public BlockState updateShape(BlockState p_57875_, Direction p_57876_, BlockState p_57877_, LevelAccessor p_57878_,
                                  BlockPos p_57879_, BlockPos p_57880_) {
        if (p_57876_ == Direction.DOWN) {
            return super.updateShape(p_57875_, p_57876_, p_57877_, p_57878_, p_57879_, p_57880_);
        } else {
            BlockState blockstate = this.getUpdatedState(p_57875_, p_57878_, p_57879_);
            return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
        }
    }

    public boolean canBeReplaced(BlockState p_57858_, BlockPlaceContext p_57859_) {
        BlockState blockstate = p_57859_.getLevel().getBlockState(p_57859_.getClickedPos());
        if (blockstate.is(this)) {
            return this.countFaces(blockstate) < PROPERTY_BY_DIRECTION.size();
        } else {
            return super.canBeReplaced(p_57858_, p_57859_);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_57849_) {
        BlockState blockstate = p_57849_.getLevel().getBlockState(p_57849_.getClickedPos());
        boolean flag = blockstate.is(this);
        BlockState blockstate1 = flag ? blockstate : this.defaultBlockState();
        Direction[] var5 = p_57849_.getNearestLookingDirections();
        for (Direction direction : var5) {
            if (direction != Direction.DOWN) {
                BooleanProperty booleanproperty = VineBlock.getPropertyForFace(direction);
                boolean flag1 = flag && blockstate.getValue(booleanproperty);
                if (!flag1 && this.canSupportAtFace(p_57849_.getLevel(), p_57849_.getClickedPos(), direction)) {
                    return blockstate1.setValue(booleanproperty, true);
                }
            }
        }
        return flag ? blockstate1 : null;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57882_) {
        p_57882_.add(UP, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
                                 InteractionHand p_60507_, BlockHitResult p_60508_) {
        for (int i = 0;i < 9; i++) {
            for (int j=0;j<9;j++) {
                RandomSource source = p_60504_.getRandom();
                int k = source.nextBoolean() ? i : -i;
                int l = source.nextBoolean() ? j : -j;
                BlockPos pos = p_60505_.offset(k, 0, l);
                Direction direction = Direction.getRandom(source);
                if (direction == Direction.DOWN || direction == Direction.UP) {
                    direction = Direction.EAST;
                }
                p_60506_.moveTo(pos.relative(direction), 0, 0);
            }
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    public BlockState mirror(BlockState p_57865_, Mirror p_57866_) {
        switch (p_57866_) {
            case LEFT_RIGHT -> {
                return p_57865_.setValue(NORTH, p_57865_.getValue(SOUTH)).setValue(SOUTH, p_57865_.getValue(NORTH));
            }
            case FRONT_BACK -> {
                return p_57865_.setValue(EAST, p_57865_.getValue(WEST)).setValue(WEST, p_57865_.getValue(EAST));
            }
            default -> {
                return super.mirror(p_57865_, p_57866_);
            }
        }
    }

    private static VoxelShape calculateShape(BlockState p_57906_) {
        VoxelShape voxelshape = Shapes.empty();
        if (p_57906_.getValue(UP)) {
            voxelshape = UP_AABB;
        }
        if (p_57906_.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }
        if (p_57906_.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }
        if (p_57906_.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }
        if (p_57906_.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }
        return voxelshape.isEmpty() ? Shapes.block() : voxelshape;
    }

    static {
        UP = PipeBlock.UP;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((
                p_57886_) -> p_57886_.getKey() != Direction.DOWN).collect(Util.toMap());
        UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_AABB =
                Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_AABB =
                Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_AABB =
                Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
