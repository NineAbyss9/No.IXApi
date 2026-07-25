
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.client.BossBar;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class DiscardItem
extends SwordItem {
    public DiscardItem() {
        super(ItemUtil.getTier(0, 8f, 112f, 3, 15, Ingredient.EMPTY),
                1, 0, new Item.Properties().fireResistant());
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof Player) {
            return super.onLeftClickEntity(stack, player, entity);
        }
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Health", 0);
        try {
            if (entity instanceof LivingEntity living) {
                living.readAdditionalSaveData(tag);
            }
        } catch (RuntimeException ignore) {
        }
        if (entity.level().isClientSide) {
            if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                BossBar.removeBossBar(apostle.getUUID(), apostle);
            }
        }
        entity.canUpdate(false);
        entity.remove(Entity.RemovalReason.KILLED);
        entity.setRemoved(Entity.RemovalReason.KILLED);
        entity.onRemovedFromWorld();
        return false;
    }

    public InteractionResult useOn(UseOnContext p_41341_) {
        return super.useOn(p_41341_);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        this.discard(pLevel, pPlayer);
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    private void discard(Level pLevel, Player pPlayer) {
        List<Entity> list = pLevel.getEntitiesOfClass(Entity.class, pPlayer.getBoundingBox().inflate(999));
        if (list.isEmpty()) {
            return;
        }
        for (Entity entity : list) {
            if (!(entity instanceof Player)) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("Health", 0);
                if (entity instanceof LivingEntity living) {
                    try {
                        living.readAdditionalSaveData(tag);
                    } catch (RuntimeException ignores) {
                        if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                            BossBar.removeBossBar(apostle.getUUID(), apostle);
                        }
                        entity.setRemoved(Entity.RemovalReason.KILLED);
                        continue;
                    }
                }
                if (pLevel.isClientSide) {
                    if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                        BossBar.removeBossBar(apostle.getUUID(), apostle);
                    }
                }
                entity.setRemoved(Entity.RemovalReason.KILLED);
            }
        }
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.NONE;
    }

    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (!(pLivingEntity instanceof Player player)) {
            return;
        }
        this.discard(pLevel, player);
    }
}
