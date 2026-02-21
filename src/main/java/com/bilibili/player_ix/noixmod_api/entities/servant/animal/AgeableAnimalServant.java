
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IAgeableMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class AgeableAnimalServant extends AnimalServant implements IAgeableMob {
    protected static final EntityDataAccessor<Integer> DATA_AGE;
    protected int inLoveTime;
    public AgeableAnimalServant(EntityType<? extends AgeableAnimalServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (this.isBaby()) {
                if (this.getAge() < this.getMaxAge()) {
                    this.grow();
                }
            }
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AGE, 24000);
    }

    public boolean isBaby() {
        return this.entityData.get(DATA_BABY);
    }

    public void spawnChildFromBreeding(ServerLevel p_27564_, IAgeableMob p_27565_) {}

    @Nullable
    public IAgeableMob getBreedMob() {
        return null;
    }

    public boolean isInLove() {
        return this.inLoveTime > 0;
    }

    public int getInLoveTime() {
        return inLoveTime;
    }

    public void setInLoveTime(int time) {
        this.inLoveTime = time;
    }

    public int getAge() {
        return this.entityData.get(DATA_AGE);
    }

    public void setAge(int age) {
        if (age >= this.getMaxAge()) {
            age = this.getMaxAge();
            this.setBaby(false);
        }
        this.entityData.set(DATA_AGE, age);
    }

    public int getMaxAge() {
        return 12000;
    }

    public void grow() {
        this.setAge(this.getAge() + 1);
    }

    static {
        DATA_AGE = SynchedEntityData.defineId(AgeableAnimalServant.class, EntityDataSerializers.INT);
    }
}
