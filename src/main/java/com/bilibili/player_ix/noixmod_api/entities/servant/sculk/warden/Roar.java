
package com.bilibili.player_ix.noixmod_api.entities.servant.sculk.warden;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class Roar<E extends WardenServant> extends Behavior<E> {
   public Roar() {
      super(ImmutableMap.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_PRESENT,
              MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType
                      .ROAR_SOUND_COOLDOWN, MemoryStatus.REGISTERED, MemoryModuleType.ROAR_SOUND_DELAY,
              MemoryStatus.REGISTERED), WardenServantAi.ROAR_DURATION);
   }

   protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
      Brain<WardenServant> brain = pEntity.getBrain();
      brain.setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_DELAY, Unit.INSTANCE, 25L);
      brain.eraseMemory(MemoryModuleType.WALK_TARGET);
      LivingEntity livingentity = pEntity.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).get();
      livingentity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, pEntity.isPowerful() ? 1 : 0));
      BehaviorUtils.lookAtEntity(pEntity, livingentity);
      pEntity.setPose(Pose.ROARING);
      pEntity.increaseAngerAt(livingentity, 20, false);
   }

   protected boolean canStillUse(ServerLevel pLevel, E pEntity, long p_217590_) {
      return true;
   }

   protected void tick(ServerLevel pLevel, E pOwner, long pGameTime)
   {
       if (!pOwner.getBrain().hasMemoryValue(MemoryModuleType.ROAR_SOUND_DELAY) && !pOwner.getBrain()
               .hasMemoryValue(MemoryModuleType.ROAR_SOUND_COOLDOWN)) {
           pOwner.getBrain().setMemoryWithExpiry(MemoryModuleType.ROAR_SOUND_COOLDOWN, Unit.INSTANCE,
                   WardenServantAi.ROAR_DURATION - 25);
           pOwner.playSound(SoundEvents.WARDEN_ROAR, 3.0F, 1.0F);
           Spells.SCULK_ZOMBIE.get().castSpell(pLevel, pOwner);
       }
   }

   protected void stop(ServerLevel pLevel, E pEntity, long pGameTime) {
      if (pEntity.hasPose(Pose.ROARING)) {
         pEntity.setPose(Pose.STANDING);
      }
      pEntity.getBrain().getMemory(MemoryModuleType.ROAR_TARGET).ifPresent(pEntity::setAttackTarget);
      pEntity.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
   }
}