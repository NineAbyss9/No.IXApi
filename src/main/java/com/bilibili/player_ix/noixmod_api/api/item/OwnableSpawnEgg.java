
package com.bilibili.player_ix.noixmod_api.api.item;

import com.github.NineAbyss9.ix_api.api.item.ApiSpawnEgg;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class OwnableSpawnEgg<T extends Mob & Ownable>
extends ApiSpawnEgg {
    public OwnableSpawnEgg(Supplier<? extends EntityType<T>> type, int backgroundColor, int highlightColor,
                           Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof SpawnerBlockEntity) {
                    SpawnerBlockEntity spawnerblockentity = (SpawnerBlockEntity)blockentity;
                    EntityType<?> type = this.getType(itemstack.getTag());
                    spawnerblockentity.setEntityId(type, level.random);
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }
            EntityType<T> entitytype = this.getType(itemstack.getTag());
            String[] sts = entitytype.getDescriptionId().replace("entity.", "").split("\\.");
            StringBuilder sb = new StringBuilder(sts[1]);///{@code sts[0]} = minecraft, {@code sts[1]} = wind_zombie
            sb.insert(0, "hostile_").insert(0, sts[0] + ":");
            ResourceLocation hostile = new ResourceLocation(sb.toString());
            Player player = pContext.getPlayer();
            if (player.isCrouching() && ForgeRegistries.ENTITY_TYPES.containsKey(hostile)) {
                entitytype = (EntityType<T>)ForgeRegistries.ENTITY_TYPES.getValue(hostile);
            }
            T t = entitytype.spawn((ServerLevel)level, itemstack, player, blockpos1, MobSpawnType.SPAWN_EGG,
                    true, !Objects.equals(blockpos, blockpos1) &&
                            direction == Direction.UP);
            if (t != null) {
                if (player.isCrouching()) {
                    t.setHostile();
                }
                itemstack.shrink(1);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, blockpos);
            }
            return InteractionResult.CONSUME;
        }
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents,
                                TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("info.noixmodapi.ownable_spawn_egg"));
    }

    public EntityType<T> getType(@Nullable CompoundTag tag) {
        return (EntityType<T>)super.getType(tag);
    }

    protected EntityType<T> getDefaultType() {
        return (EntityType<T>)super.getDefaultType();
    }
}
