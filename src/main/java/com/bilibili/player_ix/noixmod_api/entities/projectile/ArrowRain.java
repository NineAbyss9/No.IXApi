
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.NihilisticArrow;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArrowRain
extends OwnedEntity {
    private static final EntityDataAccessor<Integer> DATA_PARTICLE;
    @Nullable
    private Arrow rainArrow;
    public ArrowRain(EntityType<? extends ArrowRain> type, Level level) {
        super(type, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PARTICLE, 0);
    }

    public void tick() {
        super.tick();
        double randomX = this.getRandomX(0.8);
        double randomY = this.getRandomY();
        double randomZ = this.getRandomZ(0.8);
        if (this.level().isClientSide) {
            this.level().addParticle(this.getParticle(), randomX, randomY, randomZ,
                    Math.random() * 0.3, Math.random() * 0.3, Math.random() * 0.3);
        }
        this.summonRainArrow();
    }

    public Integer getDataParticle() {
        return this.entityData.get(DATA_PARTICLE);
    }

    public void setDataParticle(Integer integer) {
        this.entityData.set(DATA_PARTICLE, integer);
    }

    public int getDefaultLifeTime() {
        return Maths.toTick(15);
    }

    public boolean hasLife() {
        return true;
    }

    @Nullable
    public Arrow getRainArrow() {
        return this.rainArrow;
    }

    public void setRainArrow(@Nullable Arrow arrow) {
        this.rainArrow = arrow;
    }

    public void summonRainArrow() {
        Arrow rain_arrow = this.getRainArrow();
        if (rain_arrow == null) {
            return;
        }
        double randomX = this.getRandomX(0.8);
        double randomY = this.getRandomY();
        double randomZ = this.getRandomZ(0.8);
        Arrow arrow = (Arrow)rain_arrow.getType().create(this.level());
        if (arrow == null) {
            return;
        }
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            arrow.setOwner(owner);
            arrow.setEffectsFromItem(owner.getMainHandItem());
        }
        arrow.setCritArrow(this.getRainArrow().isCritArrow());
        if (arrow instanceof NihilisticArrow nihilisticArrow) {
            nihilisticArrow.setDiscardOnGround(true);
        }
        arrow.setBaseDamage(this.getRainArrow().getBaseDamage());
        arrow.moveTo(randomX, randomY, randomZ);
        this.level().addFreshEntity(arrow);
    }

    public SimpleParticleType getParticle() {
        return switch (this.getDataParticle()) {
            case 1 -> ParticleTypes.SMOKE;
            case 2 -> ParticleTypes.SOUL;
            case 3 -> ParticleTypes.LARGE_SMOKE;
            case 4 -> ParticleTypes.FALLING_LAVA;
            case 5 -> ParticleTypes.RAIN;
            case 6 -> NoixmodAPIParticleTypes.DARK_SPELL.get();
            case 7 -> NoixmodAPIParticleTypes.PURPLE_ATTACK.get();
            default -> ParticleTypes.ENTITY_EFFECT;
        };
    }

    static {
        DATA_PARTICLE = SynchedEntityData.defineId(ArrowRain.class, EntityDataSerializers.INT);
    }
}
