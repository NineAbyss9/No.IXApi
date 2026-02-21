
//换成自己的程序包
package com.bilibili.player_ix.noixmod_api.entities.boss.nameless_one;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

//暗夜之心
public class HeartOfDarkNight
extends PathfinderMob //这里可以实现巫法的IOwned接口或者直接继承Owned类等
{
    public HeartOfDarkNight(EntityType<? extends HeartOfDarkNight> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hurt(DamageSource ds, float amount) {
        if (this.getLightLevelDependentMagicValue() <= 10) {
            return false;
        } else {
            if (!ds.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && //这里填另一个会使暗夜之心受伤的伤害
                    !ds.is(DamageTypes.STARVE)) {
                return false;
            }
            return super.hurt(ds, amount);
        }
    }

    /**下面的2个方法都可以不用写，这里是为了NamelessOne类可以正常运行*/
    @Nullable
    public LivingEntity getOwner() {return null;}

    public void setOwner(@Nullable LivingEntity owner) {}

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.FOLLOW_RANGE, 42)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MAX_HEALTH, 6).add(Attributes.MOVEMENT_SPEED, 0);
    }
}
