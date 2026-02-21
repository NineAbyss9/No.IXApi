
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.servant.sculk.SculkZombie;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

import static com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob.ownerOrThis;

public class Roar<E extends WardenServant> extends Behavior<E> {
   public Roar() {
      super(ImmutableMap.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_PRESENT,
              MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType
                      .ROAR_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType.ROAR_SOUND_DELAY,
              MemoryStatus.REGISTERED), WardenServantAi.ROAR_DURATION);
   }

   protected void start(ServerLevel p_217580_, E pEntity, long p_217582_) {
      Brain<WardenServant> brain = pEntity.getBrain();
      brain.setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_DELAY, Unit.INSTANCE, 25L);
      brain.eraseMemory(MemoryModuleType.WALK_TARGET);
      LivingEntity livingentity = pEntity.getBrain().getMemory(MemoryModuleType.ROAR_TARGET)
              .orElseThrow();
      livingentity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, pEntity.isPowerful() ? 1 : 0));
      BehaviorUtils.lookAtEntity(pEntity, livingentity);
      pEntity.setPose(Pose.ROARING);
      pEntity.increaseAngerAt(livingentity, 20, false);
   }

   protected boolean canStillUse(ServerLevel pLevel, E pEntity, long p_217590_) {
      return true;
   }

   protected void tick(ServerLevel p_217596_, E pOwner, long p_217598_) {
      if (!pOwner.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_DELAY) && !pOwner.getBrain()
              .hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) {
          pOwner.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE,
                  WardenServantAi.ROAR_DURATION - 25);
          pOwner.playSound(SoundEvents.WARDEN_ROAR, 3.0F, 1.0F);
          if (OwnerSummon.canSummon(p_217596_, ownerOrThis(pOwner), 8, e -> e instanceof SculkZombie))
              for (int i = 0;i < 4;i++) {
                  SculkZombie zombie = NoixmodAPIEntities.SCULK_ZOMBIE.get().create(p_217596_);
                  if (zombie != null) {
                      zombie.setOwner(ownerOrThis(pOwner));
                      zombie.moveTo(new Vec3(pOwner.getX() + Maths.randomInt(4), pOwner.getY(), pOwner.getZ()
                              + Maths.randomInt(4)));
                      zombie.finalizeSpawn(p_217596_, p_217596_.getCurrentDifficultyAt(pOwner.blockPosition()),
                              MobSpawnType.MOB_SUMMONED);
                      p_217596_.addFreshEntity(zombie);
                  }
              }
      }
   }

   protected void stop(ServerLevel p_217604_, E pEntity, long p_217606_) {
      if (pEntity.hasPose(Pose.ROARING)) {
         pEntity.setPose(Pose.STANDING);
      }
      pEntity.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(pEntity::setAttackTarget);
      pEntity.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
   }
}