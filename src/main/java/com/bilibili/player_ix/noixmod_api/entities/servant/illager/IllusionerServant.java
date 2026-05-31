
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.api.mobs.effect.EffectInstance;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NormalCastingSpellGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IllusionerServant
extends OwnableIllager
implements ApiRangedAttackMob {
    private int clientSideIllusionTicks;
    private final Vec3[][] clientSideIllusionOffsets;
    public IllusionerServant(EntityType<? extends IllusionerServant> entityType, Level level) {
        super(entityType, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.BOW));
        this.xpReward = 5;
        this.clientSideIllusionOffsets = new Vec3[2][4];
        for (int i = 0; i < 4; ++i) {
            this.clientSideIllusionOffsets[0][i] = Vec3.ZERO;
            this.clientSideIllusionOffsets[1][i] = Vec3.ZERO;
        }
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && this.isInvisible()) {
            --this.clientSideIllusionTicks;
            if (this.clientSideIllusionTicks < 0) {
                this.clientSideIllusionTicks = 0;
            }
            if (this.hurtTime != 1 && this.tickCount % 1200 != 0) {
                if (this.hurtTime == this.hurtDuration - 1) {
                    this.clientSideIllusionTicks = 3;
                    for (int k = 0; k < 4; ++k) {
                        this.clientSideIllusionOffsets[0][k] = this.clientSideIllusionOffsets[1][k];
                        this.clientSideIllusionOffsets[1][k] = new Vec3(0.0, 0.0, 0.0);
                    }
                }
            } else {
                this.clientSideIllusionTicks = 3;
                int l;
                for (l = 0; l < 4; ++l) {
                    this.clientSideIllusionOffsets[0][l] = this.clientSideIllusionOffsets[1][l];
                    this.clientSideIllusionOffsets[1][l] = new Vec3(-6.0F + this.random.nextInt(13) * 0.5,
                            Math.max(0, this.random.nextInt(6) - 4), (-6.0F + this.random.nextInt(13)) * 0.5);
                }
                for (l = 0; l < 16; ++l) {
                    this.level().addParticle(ParticleTypes.CLOUD, this.getRandomX(0.5), this.getRandomY(),
                            this.getZ(0.5), 0.0, 0.0, 0.0);
                }
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE,
                        this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.addBehaviorGoal(4, 0.7, 10.0F);
    }

    protected void addAttackGoal() {
        this.goalSelector.addGoal(0, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(1, new MirrorSpellGoal(this));
        this.goalSelector.addGoal(2, new ApiRangedBowAttackGoal(this, 0.65, 15,
                17.0F));
    }

    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0, 0.0, 3.0);
    }

    public Vec3[] getIllusionOffsets(float pPartialTick) {
        if (this.clientSideIllusionTicks <= 0) {
            return this.clientSideIllusionOffsets[1];
        } else {
            double d0 = (this.clientSideIllusionTicks - pPartialTick) / 3.0F;
            d0 = Math.pow(d0, 0.25);
            Vec3[] avec3 = new Vec3[4];
            for (int i = 0; i < 4; ++i) {
                avec3[i] = this.clientSideIllusionOffsets[1][i].scale(1.0 - d0).add(this.clientSideIllusionOffsets[0][i].scale(d0));
            }
            return avec3;
        }
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this,
                (item) -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, pDistanceFactor);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (14 - this.level()
                .getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F /
                (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, stack, pDistanceFactor);
        if (this.getMainHandItem().getItem() instanceof BowItem item) {
            abstractarrow = item.customArrow(abstractarrow);
        }
        return abstractarrow;
    }

    private static class MirrorSpellGoal extends AbstractUseSpellGoal {
        IllusionerServant illusionerServant;
        public MirrorSpellGoal(IllusionerServant finder) {
            super(finder);
            illusionerServant = finder;
        }

        protected void castSpell() {
            illusionerServant.addEffect(EffectInstance.create(MobEffects.INVISIBILITY, 1200));
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 340;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.WATER;
        }
    }
}
