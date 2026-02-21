
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiCrossbowAttackMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiCrossbowAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class PillagerServant
extends OwnableIllager
implements ApiCrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW;
    public PillagerServant(EntityType<? extends PillagerServant> entityType, Level level) {
        super(entityType, level);
        ItemStack stack = ItemStacks.of(Items.CROSSBOW);
        EnchantmentHelper.enchantItem(level.random, stack, 3, true);
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
        this.setCanPickUpLoot(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new ApiCrossbowAttackGoal<>(
                this, 1.0, 12.0F) {
            protected boolean likeArcher() {
                return true;
            }
        });
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class,
                12F));
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_21430_) {
        return p_21430_ == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean b) {
        this.entityData.set(IS_CHARGING_CROSSBOW, b);
    }

    public void performRangedAttack(LivingEntity p_33272_, float p_33273_) {
        this.performCrossbowAttack(this,
                1.6F);
    }

    public void shootCrossbowProjectile(LivingEntity p_33275_, ItemStack p_33276_, Projectile p_33277_, float p_33278_) {
        this.shootCrossbowProjectile(this, p_33275_, p_33277_, p_33278_, 1.6F);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public ApiPose getPoses() {
        if (this.isChargingCrossbow()) {
            return ApiPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return ApiPose.CROSSBOW_HOLD;
        } else
        return this.isAggressive() ? ApiPose.ATTACKING : ApiPose.NATURAL;
    }

    public static AttributeSupplier createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3499999940395355)
                .add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 32.0).build();
    }

    static {
        IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(PillagerServant.class, EntityDataSerializers.BOOLEAN);
    }
}
