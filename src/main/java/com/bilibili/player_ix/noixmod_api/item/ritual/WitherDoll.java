
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticWitherBoss;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WitherDoll
extends Item {
    public WitherDoll() {
        super(new Item.Properties().rarity(Rarity.RARE));
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41432_.isClientSide) {
            p_41433_.playSound(SoundEvents.EVOKER_CAST_SPELL);
        } else {
            boolean flag = false;
            if (p_41432_.dimension() == Level.NETHER) {
                WitherBoss boss = EntityType.WITHER.create(p_41432_);
                if (boss != null) {
                    boss.moveTo(p_41433_.position());
                    boss.makeInvulnerable();
                    flag = p_41432_.addFreshEntity(boss);
                }
            } else {
                NihilisticWitherBoss wither = NoixmodAPIEntities.NIHILISTIC_WITHER_BOSS.get().create(p_41432_);
                if (wither != null) {
                    ServerLevel level = (ServerLevel)p_41432_;
                    wither.moveTo(p_41433_.position());
                    wither.finalizeSpawn(level, level.getCurrentDifficultyAt(p_41433_.blockPosition()), MobSpawnType.EVENT);
                    flag = level.addFreshEntity(wither);
                }
            }
            if (flag) {
                p_41433_.getCooldowns().addCooldown(this, 40);
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }
}
