
package com.bilibili.player_ix.noixmod_api.mob_effects;

import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.entities.servant.VampireServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.npc.AbstractVillager;

public class DesireForBlood extends MobEffect {
    public DesireForBlood() {
        super(MobEffectCategory.HARMFUL, -12189696);
    }

    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return false;
    }

    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
    {
    }

    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pMap, int pAmplifier) {
        if (pLivingEntity instanceof AbstractVillager villager) {
            if (java.util.concurrent.ThreadLocalRandom.current().nextBoolean()) {
                if (!pLivingEntity.level().isClientSide) {
                    EntityEventHandler.broadcastEntityEvent(villager, 4);
                }
                villager.kill();
            } else {
                if (!pLivingEntity.level().isClientSide) {
                    VampireServant servant = NoixmodAPIEntities.VAMPIRE_SERVANT.get().spawn((ServerLevel)pLivingEntity.level(),
                            villager.blockPosition(), MobSpawnType.CONVERSION);
                    if (servant != null) {
                        servant.setOwner(pLivingEntity.level().getNearestPlayer(villager, 30.0));
                    }
                }
                pLivingEntity.remove(Entity.RemovalReason.KILLED);
            }
        } else if (pLivingEntity instanceof ApiVillager) {
            if (pLivingEntity.getRandom().nextBoolean()) {
                if (!pLivingEntity.level().isClientSide) {
                    pLivingEntity.hurt(pLivingEntity.damageSources().magic(), pLivingEntity.getMaxHealth() / 2);
                }
            } else {
                if (!pLivingEntity.level().isClientSide) {
                    ServerLevel level = (ServerLevel)pLivingEntity.level();
                    VampireServant servant = NoixmodAPIEntities.VAMPIRE_SERVANT.get().spawn(level,
                            pLivingEntity.blockPosition(), MobSpawnType.CONVERSION);
                    if (servant != null) {
                        servant.setOwner(level.getNearestPlayer(pLivingEntity, 30.0));
                    }
                }
                pLivingEntity.remove(Entity.RemovalReason.KILLED);
            }
        }
        super.removeAttributeModifiers(pLivingEntity, pMap, pAmplifier);
    }
}
