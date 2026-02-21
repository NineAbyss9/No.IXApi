
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.entities.servant.MagicalClone;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class MagicalSword
extends SwordItem {
    private int cooldown;
    public MagicalSword() {
        super(ItemUtil.getTier(
                0, 2.8f, 5, 3, 5, Ingredient.EMPTY
        ), 1, -2.4f, new Properties().rarity(Rarity.RARE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_43243_, Player p_43244_, InteractionHand p_43245_) {
        p_43244_.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
        for (int i = 0; i < 5;++i) {
            if (p_43243_ instanceof ServerLevel serverLevel) {
                MagicalClone clone = new MagicalClone(NoixmodAPIEntities.MAGICAL_CLONE.get(), p_43243_);
                int d = Maths.randomInteger(3);
                int d1 = Maths.randomInteger(3);
                BlockPos.MutableBlockPos pos = p_43244_.blockPosition().offset(d, 0, d1).mutable();
                clone.setOwner(p_43244_);
                clone.moveTo(pos, 0, 0);
                clone.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED);
                serverLevel.addFreshEntity(clone);
                clone.spawnAnim();
            }
            p_43244_.getCooldowns().addCooldown(this, 60);
        }
        return ItemUtils.startUsingInstantly(p_43243_, p_43244_, p_43245_);
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 72000;
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (this.getCooldown() <= 0) {
            if (entity instanceof LivingEntity lie && lie.isAlive()) {
                lie.setHealth(lie.getHealth() - (lie.getMaxHealth() / 50));
                this.setCooldown(2);
            }
        } else {
            if (entity instanceof LivingEntity lie && lie.hurtDuration > 9) {
                --this.cooldown;
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BLOCK;
    }
}
