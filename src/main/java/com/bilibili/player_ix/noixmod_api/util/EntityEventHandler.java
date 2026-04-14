
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.api.mobs.IShieldUser;
import org.NineAbyss9.util.Option;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.monster.illager.Mourner;
import com.bilibili.player_ix.noixmod_api.register.ErrorCodes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**The {@linkplain EntityEventHandler} class provides prepared codes for coders.
 * The method {@linkplain #broadcastEntityEvent(Entity, int)} is the main content.For example:
 * <blockquote><pre>
 *     {@code if (!this.level().isClientSide) EntityEventHandler.broadcastEntityEvent(this, 0);}
 * </pre></blockquote>
 * @author Player_IX*/
public class EntityEventHandler {
    public EntityEventHandler() {
    }

    public static void broadcastEntityEvent(Entity entity, int event) {
        switch (event) {
            case 0 :{
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.SMOKE,
                        entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.5, 0.5,
                        0.5, 0.1);
                break;
            }
            case 1: {
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.CRIT,
                        entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.5, 0.5,
                        0.5, 0.1);
                break;
            }
            case 2: {
                ((ServerLevel)entity.level()).sendParticles(NoixmodAPIParticleTypes.GOLDEN_FLAME.get(),
                        entity.getX(), entity.getY(), entity.getZ(), 30, 1, 2, 1, 0);
                break;
            }
            case 3: {
                if (entity instanceof LivingEntity living) {
                    MobEffectInstance instance = living.getEffect(NoixmodAPIMobEffects.TETANUS.get());
                    if (living.hasEffect(NoixmodAPIMobEffects.TETANUS.get()) && instance != null) {
                        living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.TETANUS.get(), 60,
                                instance.getAmplifier() + 1));
                    } else {
                        living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.TETANUS.get(), 60, 0));
                    }
                }
                break;
            }
            case 4: {
                ((ServerLevel)entity.level()).sendParticles(NoixmodAPIParticleTypes.BLOOD.get(),
                        entity.getX(), entity.getY() + 1, entity.getZ(), 30, 0.5, 0.5,
                        0.5, 0.1);
                break;
            }
            case 5: {
                if (entity instanceof Mourner mourner) {
                    Option<LivingEntity> var10000 = Option.ofNullable(mourner.getTarget());
                    var10000.filter(living ->  MobUtils.canHurt(living, mourner)).filter((p_217707_) ->
                            mourner.closerThan(p_217707_, 15.0, 20.0)).ifPresent((p_217704_) -> {
                        Vec3 $$3 = mourner.position().add(0.0, 1.600000023841858, 0.0);
                        Vec3 $$4 = p_217704_.getEyePosition().subtract($$3);
                        Vec3 $$5 = $$4.normalize();
                        for (int $$6 = 1; $$6 < Mth.floor($$4.length()) + 7; ++$$6) {
                            Vec3 $$7 = $$3.add($$5.scale($$6));
                            ((ServerLevel)p_217704_.level()).sendParticles(ParticleTypes.SOUL, $$7.x, $$7.y - 0.5, $$7.z,
                                    7, 1.5, 1.5, 1.5, 0.0);
                            ((ServerLevel)p_217704_.level()).sendParticles(ParticleTypes.SONIC_BOOM, $$7.x, $$7.y, $$7.z,
                                    1, 0.0, 0.0, 0.0, 0.0);
                        }
                        mourner.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                        p_217704_.hurt(mourner.damageSources().mobAttack(mourner),
                                Math.min(NoixmodAPIMainConfig.MournerDamage.get().floatValue(), mourner.getDeath()));
                        double $$8 = 0.5 * (1.0 - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        double $$9 = 2.5 * (1.0 - p_217704_.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        p_217704_.push($$5.x() * $$9, $$5.y() * $$8, $$5.z() * $$9);
                    });
                    break;
                }
            }
            case 6: {
                if (entity instanceof Mob mob) {
                    Option<LivingEntity> var10000 = Option.ofNullable(mob.getTarget());
                    var10000.filter(living ->  MobUtils.canHurt(living, mob)).filter(target ->
                            mob.closerThan(target, 35)).ifPresent((target) -> {
                        Vec3 $$3 = mob.position().add(0.0, 1.600000023841858, 0.0);
                        Vec3 $$4 = target.getEyePosition().subtract($$3);
                        Vec3 $$5 = $$4.normalize();
                        for (int $$6 = 1; $$6 < Mth.floor($$4.length()) + 3; ++$$6) {
                            Vec3 $$7 = $$3.add($$5.scale($$6));
                            ((ServerLevel)target.level()).sendParticles(ParticleTypes.SMOKE, $$7.x, $$7.y, $$7.z, 6,
                                    0.5, 0.5, 0.5, 0.0);
                        }
                        if (!target.isBlocking()) {
                            target.setHealth(target.getHealth() - Maths.healthLessThan(target, 5, 30));
                            target.hurt(mob.damageSources().indirectMagic(mob, mob),
                                    8f);
                        }
                        List<LivingEntity> list = target.level().getEntitiesOfClass(LivingEntity.class, target
                                .getBoundingBox().inflate(2), living -> MobUtils.canHurt(living, mob));
                        for (LivingEntity living : list) {
                            if (living.isBlocking()) {
                                IShieldUser.hurtShield(living, 9);
                                MobUtils.disableShield(1, 2, 1, living);
                                living.playSound(SoundEvents.SHIELD_BLOCK);
                            } else {
                                living.hurt(mob.damageSources().indirectMagic(mob, mob), 12f);
                            }
                        }
                    });
                }
                break;
            }
            default:{
                NoixmodAPI.LOGGER.warn("Cannot handle event {} in EntityEventHandler.Error code: {}",
                        event, ErrorCodes.ENTITY_EVENT_HANDLER);
                break;
            }
        }
    }

    @ServerOnly
    public static void wardenBoom(Entity entity, DamageSource source, LivingEntity var1, double var2) {
        Option<LivingEntity> var10000 = Option.of(var1);
        var10000.filter(living ->  MobUtils.canHurt(living, entity)).filter((target) ->
                entity.closerThan(target, Mth.square(var2))).ifPresent((target) -> {
            Vec3 $$3 = entity.position().add(0.0, 1.600000023841858, 0.0);
            Vec3 $$4 = target.getEyePosition().subtract($$3);
            Vec3 $$5 = $$4.normalize();
            for (int $$6 = 1; $$6 < Mth.floor($$4.length()) + 7; ++$$6) {
                Vec3 $$7 = $$3.add($$5.scale($$6));
                ((ServerLevel)target.level()).sendParticles(ParticleTypes.SMOKE, $$7.x, $$7.y, $$7.z, 1,
                        0.0, 0.0, 0.0, 0.0);
            }
            if (target.isBlocking()) {
                MobUtils.disableShield(target, 60);
                target.playSound(SoundEvents.SHIELD_BLOCK);
            } else {
                target.setHealth(target.getHealth() - 15);
                target.hurt(source, 8f);
            }
        });
    }
}
