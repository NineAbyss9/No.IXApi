
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class AnimalServant extends OwnableMob {
    public AnimalServant(EntityType<? extends AnimalServant> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    public float getWalkTargetValue(BlockPos p_27573_, LevelReader p_27574_) {
        return p_27574_.getBlockState(p_27573_.below()).is(Blocks.GRASS_BLOCK) ? 10.0F :
                p_27574_.getPathfindingCostFromLightLevels(p_27573_);
    }

    public double getMyRidingOffset() {
        return 0.14;
    }

    public int getAmbientSoundInterval() {
        return 120;
    }

    public boolean removeWhenFarAway(double p_27598_) {
        return false;
    }

    public int getExperienceReward() {
        return 1 + this.level().random.nextInt(3);
    }
}
