
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
        return true;
    }

    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        if (p_19469_ instanceof AbstractVillager villager) {
            if (villager.getRandom().nextBoolean()) {
                if (!p_19469_.level().isClientSide) {
                    EntityEventHandler.broadcastEntityEvent(villager, 4);
                }
                villager.kill();
            } else {
                if (p_19469_.level() instanceof ServerLevel level) {
                    VampireServant servant = NoixmodAPIEntities.VAMPIRE_SERVANT.get().spawn(level,
                            villager.blockPosition(), MobSpawnType.CONVERSION);
                    if (servant != null) {
                        servant.setOwner(level.getNearestPlayer(villager, 30.0));
                    }
                }
                p_19469_.remove(Entity.RemovalReason.KILLED);
            }
        } else if (p_19469_ instanceof ApiVillager) {
            if (p_19469_.getRandom().nextBoolean()) {
                if (!p_19469_.level().isClientSide) {
                    p_19469_.hurt(p_19469_.damageSources().magic(), p_19469_.getMaxHealth() / 2);
                }
            } else {
                if (p_19469_.level() instanceof ServerLevel level) {
                    VampireServant servant = NoixmodAPIEntities.VAMPIRE_SERVANT.get().spawn(level,
                            p_19469_.blockPosition(), MobSpawnType.CONVERSION);
                    if (servant != null) {
                        servant.setOwner(level.getNearestPlayer(p_19469_, 30.0));
                    }
                }
                p_19469_.remove(Entity.RemovalReason.KILLED);
            }
        }
        super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
    }
}
