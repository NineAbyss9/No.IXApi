
package com.bilibili.player_ix.noixmod_api.entities.projectile.summon;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.boss.ApostleBoss;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.ApostleServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;

public class SummonApostle
extends OwnedEntity {
    private boolean isBoss = true;
    public SummonApostle(EntityType<? extends SummonApostle> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public EntityType<?> getType() {
        return super.getType();
    }

    public void tick() {
        super.tick();
        if (this.getLifeTick() > 0) {
            ParticleUtil.addFlatParticle(ParticleTypes.SMOKE, this, 0.8, 0.8);
            ParticleUtil.addFlatParticle(ParticleTypes.LARGE_SMOKE, this, 0.6, 0.6);
        } else {
            this.summonBoss();
            this.discard();
        }
    }

    public boolean hasLife() {
        return true;
    }

    public int getDefaultLifeTime() {
        return Maths.toTick(20);
    }

    public void setBoss(boolean boss) {
        this.isBoss = boss;
    }

    public void summonBoss() {
        if (!this.level().isClientSide) {
            this.playSound(SoundEvents.FIRE_EXTINGUISH, 1F, 1F);
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
            bolt.setVisualOnly(true);
            bolt.moveTo(this.blockPosition(), 0, 0);
            this.level().addFreshEntity(bolt);
            WorldUtil.sendParticles(ParticleTypes.LARGE_SMOKE, this, 70,
                    this.random.nextGaussian() * 0.3);
            WorldUtil.sendParticles(ParticleTypes.SOUL, this, 70, this.random.nextGaussian() * 0.2);
            ServerLevel level = (ServerLevel)this.level();
            level.sendParticles(NoixmodAPIParticleTypes.PURPLE_FLAME.get(),
                    this.getX(), this.getY(), this.getZ(), 19, 1.5, 1.5, 1.5, 0);
            if (this.isBoss) {
                ApostleBoss boss = new ApostleBoss(NoixmodAPIEntities.APOSTLE.get(), level);
                boss.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.EVENT);
                boss.moveTo(this.blockPosition(), 0, 0);
                level.addFreshEntity(boss);
            } else {
                ApostleServant servant = new ApostleServant(NoixmodAPIEntities.APOSTLE_SERVANT.get(), level);
                if (this.getOwner() instanceof ApostleBoss boss) {
                    servant.setArrowDamage(boss.getArrowDamage());
                }
                servant.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.EVENT);
                servant.moveTo(this.blockPosition(), 0, 0);
                servant.setOwner(this.getOwner());
                level.addFreshEntity(servant);
            }
        }
    }
}
