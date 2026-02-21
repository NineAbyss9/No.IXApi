
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.IProjectile;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.NihilisticArrow;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NihilisticArrowRain
extends ArrowRain
implements IProjectile {
    public NihilisticArrowRain(EntityType<? extends ArrowRain> type, Level level) {
        super(type, level);
        this.setDataParticle(7);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.NIHILISTIC_ARROW_RAIN.get();
    }

    public void tick() {
        if (this.level().isClientSide()) {
            this.level().addParticle(NoixmodAPIParticleTypes.PURPLE_ATTACK.get(), this.getRandomX(0.8), this.getRandomY(), this.getRandomZ(0.8), this.random.nextGaussian() * 0.3, this.random.nextGaussian() * 0.3, this.random.nextGaussian() * 0.3);
        }
        super.tick();
    }

    public void summonRainArrow() {
        for (int i = 0; i < 3; ++i) {
            NihilisticArrow arrow = new NihilisticArrow(NoixmodAPIEntities.NIHILISTIC_ARROW.get(), this.level());
            arrow.setOwner(this.getOwner());
            arrow.setDeltaMovement(new Vec3(0, -0.3, 0));
            if (this.getOwner() != null) {
                arrow.setEffectsFromItem(this.getOwner().getMainHandItem());
                arrow.setEnchantmentEffectsFromEntity(this.getOwner(), 5f);
            }
            if (this.getOwner() instanceof Apostle apostle) {
                arrow.setCritArrow(apostle.isInEnd());
                arrow.setBaseDamage(apostle.getArrowDamage());
            } else {
                arrow.setCritArrow(this.random.nextFloat() <= 0.05f);
            }
            arrow.setFlag(true);
            arrow.moveTo(this.getRandomX(0.8), this.getRandomY(), this.getRandomZ(0.8));
            this.level().addFreshEntity(arrow);
        }
    }

    public void onHit(HitResult result) {
    }

    public void onHitEntity(EntityHitResult pResult) {
    }

    public void onHitBlock(BlockHitResult pResult) {
    }

    public boolean canHitEntity(Entity pEntity) {
        return true;
    }
}
