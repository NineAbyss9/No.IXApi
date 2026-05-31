
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.NineAbyss9.math.MathSupport;

public class BloodiedSword extends SwordItem {
    public BloodiedSword() {
        super(ItemUtil.getTier(
                0, 2.8f, 7, 3, 5, Ingredient.EMPTY
        ), 1, -2.4f, new Properties().stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
        if (p_43279_.level() instanceof ServerLevel level) {
            level.sendParticles(NoixmodAPIParticleTypes.BLOOD.get(), p_43279_.getX(), p_43279_.getY(), p_43279_.getZ(),
                    10, 0, 0, 0, java.util.concurrent.ThreadLocalRandom.current().nextGaussian() * 0.2);
        }
        return super.hurtEnemy(p_43278_, p_43279_, p_43280_);
    }
}
