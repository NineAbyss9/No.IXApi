
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

//Repair use InfernalIron
public class InfernalIronSword
extends ApiSword {
    public InfernalIronSword() {
        super(ApiTier.NETHERITE, 4, -2.3f,
                new Properties().fireResistant().stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity enemy, LivingEntity p_43280_) {
        enemy.setSecondsOnFire(2);
        enemy.playSound(SoundEvents.FIRE_EXTINGUISH);
        if (enemy.level() instanceof ServerLevel level) {
            ParticleUtil.sendParticles(level, ParticleTypes.FLAME, enemy.position(), 9, 1.5, 2, 1.5, 0);
        }
        return super.hurtEnemy(p_43278_, enemy, p_43280_);
    }
}
