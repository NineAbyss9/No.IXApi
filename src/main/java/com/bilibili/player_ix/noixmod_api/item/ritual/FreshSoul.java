
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.entities.servant.GirlGhost;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FreshSoul extends RitualSupplies {
    public FreshSoul() {
        super(new Properties().stacksTo(64).fireResistant());
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41434_.equals(InteractionHand.MAIN_HAND)) {
            GirlGhost ghost = new GirlGhost(NoixmodAPIEntities.GIRL_GHOST.get(), p_41432_);
            ghost.setOwner(p_41433_);
            ghost.moveTo(p_41433_.blockPosition().offset(Maths.randomInt(3), 0, Maths.randomInt(3)),
                    0, 0);
            if (p_41432_ instanceof ServerLevel level) {
                ghost.finalizeSpawn(level, level.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED);
            }
            p_41432_.addFreshEntity(ghost);
            ItemUtil.shrink(p_41433_.getMainHandItem(), p_41433_);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
