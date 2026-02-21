
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.client.BossBar;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticLord;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
        if (!(entity instanceof Player)) {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("Health", 0);
            try {
                if (entity instanceof LivingEntity living) {
                    living.readAdditionalSaveData(tag);
                }
            } catch (RuntimeException ignores) {
                //Empty catch block
            }
            if (entity.level().isClientSide) {
                if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                    BossBar.removeBossBar(apostle.getUUID(), apostle);
                }
            }
            entity.remove(Entity.RemovalReason.KILLED);
            entity.canUpdate(false);
            entity.setRemoved(Entity.RemovalReason.KILLED);
            entity.onRemovedFromWorld();
            if (!entity.isRemoved()) {
                if (entity.level() instanceof ServerLevel level) {
                    level.getEntitiesOfClass(entity.getClass(), entity.getBoundingBox().inflate(6)).remove(entity);
                    level.getEntities(entity, entity.getBoundingBox().inflate(6)).remove(entity);
                }
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public InteractionResult useOn(UseOnContext p_41341_) {
        if (p_41341_.getPlayer() != null) {
            List<Entity> list = WorldUtil.entityList(Entity.class, p_41341_.getPlayer(), 365, 365, 365);
            for (Entity entity : list) {
                if (!(entity instanceof Player)) {
                    entity.discard();
                    if (entity instanceof NihilisticLord lord) {
                        lord.setRemoved(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
        return super.useOn(p_41341_);
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        List<Entity> list = p_41432_.getEntitiesOfClass(Entity.class, p_41433_.getBoundingBox().inflate(999));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!(entity instanceof Player)) {
                    CompoundTag tag = new CompoundTag();
                    tag.putFloat("Health", 0);
                    if (entity instanceof LivingEntity living) {
                        try {
                            living.readAdditionalSaveData(tag);
                        } catch (RuntimeException ignores) {
                            entity.setRemoved(Entity.RemovalReason.KILLED);
                            if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                                BossBar.removeBossBar(apostle.getUUID(), apostle);
                            }
                            continue;
                        }
                    }
                    if (entity instanceof Apostle apostle && BossBar.contains(apostle.getUUID())) {
                        BossBar.removeBossBar(apostle.getUUID(), apostle);
                    }
                    entity.setRemoved(Entity.RemovalReason.KILLED);
                }
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.NONE;
    }

    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if (p_41411_ instanceof Player player) {
            List<Entity> list = WorldUtil.entityList(Entity.class, player, 365, 365, 365);
            for (Entity entity : list) {
                if (!(entity instanceof Player)) {
                    entity.discard();
                }
            }
        }
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
        if (p_41429_ instanceof Player player) {
            List<Entity> list = WorldUtil.entityList(Entity.class, player, 365, 365, 365);
            if (list.isEmpty()) {
                return;
            }
            for (Entity entity : list) {
                if (!(entity instanceof Player)) {
                    entity.discard();
                }
            }
        }
    }
}
