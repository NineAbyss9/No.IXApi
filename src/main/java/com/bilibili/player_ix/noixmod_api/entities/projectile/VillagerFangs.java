
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiVillager;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class VillagerFangs
extends OwnedEntity {
    private int lifeTicks;
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private boolean clientSideAttackStarted;
    public VillagerFangs(EntityType<? extends VillagerFangs> type, Level level) {
        super(type, level);
        this.lifeTicks = 22;
    }

    public void tick() {
        if (this.level().isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for (int i = 0; i < 5; ++i) {
                        this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.defaultBlockState()),
                                this.getX(), this.getY() + 1, this.getZ(), this.random.nextGaussian() * 0.2,
                                this.random.nextGaussian() * 0.2, this.random.nextGaussian() * 0.2);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5, 0, 0.5));
                for (LivingEntity living : list) {
                    if (canDamage(living, this)) {
                        this.hurtEntity(living);
                    }
                }
            }
            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }
            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }
        super.tick();
    }

    public void handleEntityEvent(byte p_36935_) {
        super.handleEntityEvent(p_36935_);
        if (p_36935_ == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.METAL_BREAK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    public void setWarmupDelayTicks(int ticks) {
        this.warmupDelayTicks = ticks;
    }

    public static boolean canDamage(LivingEntity living, @Nullable Entity entity) {
        if (entity instanceof ApiVillager || entity instanceof VillagerFangs) {
            if (NoixmodAPIMainConfig.VILLAGERS_IGNORE.get().stream().anyMatch(s -> {
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(s));
                return type != null && type == living.getType();
            })) {
                return false;
            }
            if (living instanceof ApiVillager || living instanceof AbstractVillager) {
                return false;
            }
            if (living instanceof AbstractGolem) {
                return false;
            }
            return MobUtils.canHurt(living, entity);
        } else {
            return true;
        }
    }

    public float getAnimationProgress(float p_36937_) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int $$1 = this.lifeTicks - 2;
            return $$1 <= 0 ? 1.0F : 1.0F - ((float)$$1 - p_36937_) / 20.0F;
        }
    }

    public void hurtEntity(@NotNull LivingEntity target) {
        target.push(0, 1.1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0);
        target.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 8F);
    }
}
