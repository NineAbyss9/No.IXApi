
package com.bilibili.player_ix.noixmod_api.item.grave;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.item.weapon.ApiSword;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import org.NineAbyss9.math.MathSupport;

public class GraveSword
extends ApiSword
implements IGraveItem
{
    public GraveSword() {
        super(1599, 5f, 4.9f, 11, 10, Ingredient.EMPTY, 3,
                -2.6f, new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).fireResistant());
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide) {
            if (MathSupport.random.nextFloat() < 0.25F) {
                pAttacker.playSound(SoundEvents.SOUL_ESCAPE, 0.6f, 1f);
                if (pTarget.isAlive()) {
                    pTarget.setHealth(Math.max(pTarget.getHealth() - 5.0F, 0.0F));
                }
                ParticleUtil.sendParticles((ServerLevel)pAttacker.level(), ParticleTypes.SOUL, pTarget.position(),
                        8, 1, 1, 1, 0);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
