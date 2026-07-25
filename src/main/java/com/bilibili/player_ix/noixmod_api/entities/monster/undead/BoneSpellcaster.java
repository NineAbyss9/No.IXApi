
package com.bilibili.player_ix.noixmod_api.entities.monster.undead;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.*;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.AbstractUseSpellGoal;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.NormalCastingSpellGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.concurrent.ThreadLocalRandom;

public class BoneSpellcaster
extends OwnableMob
implements ApiPoseMob, SpellCasterMob
{
    protected static final EntityDataAccessor<Integer> DATA_SPELL_TICK;
    protected static final EntityDataAccessor<APISpells.APISpell> DATA_SPELL;
    protected final OwnerSummon ownerSummon = new OwnerSummon(this);
    public BoneSpellcaster(EntityType<? extends BoneSpellcaster> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
        if (pLevel.getDifficulty() == Difficulty.HARD)
        {
            this.populateDefaultEquipmentSlots(pLevel.random, pLevel.getCurrentDifficultyAt(this.blockPosition()));
        }
        this.setHostile();
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_SPELL, APISpells.APISpell.NONE);
        this.entityData.define(DATA_SPELL_TICK, 0);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(2, new SummonSpellGoal(this));
        this.goalSelector.addGoal(3, new AttackSpellGoal(this));
        this.addBehaviorGoal(4, 0.6D, 10.0F);
        targetSelector.addGoal(1, new HurtByTargetGoal(this, BoneSpellcaster.class));
        targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, true));
    }

    public void aiStep()
    {
        super.aiStep();
        if (this.level().isClientSide && this.isCastingSpell()) {
            APISpells.APISpell spellId = this.getSpellType();
            double $$1 = spellId.spellColor[0];
            double $$2 = spellId.spellColor[1];
            double $$3 = spellId.spellColor[2];
            float $$4 = this.yBodyRot * ((float) Math.PI / 180) + Mth.cos((float) this.tickCount * 0.6662f) * 0.25f;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + $$5 * 0.6D,
                    this.getY() + 1.8D, this.getZ() + $$6 * 0.6D, $$1, $$2, $$3);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - $$5 * 0.6D,
                    this.getY() + 1.8D, this.getZ() - $$6 * 0.6D, $$1, $$2, $$3);
        }
    }

    protected void customServerAiStep()
    {
        super.customServerAiStep();
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit)
    {
        if (this.mobData.getDifficultyInstance().isHard() || ThreadLocalRandom.current().nextBoolean())
        {
            this.spawnAtLocation(NoixmodAPIItems.BONE_STAFF);
        }
        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            if (equipmentslot == EquipmentSlot.MAINHAND) continue;
            ItemStack itemstack = this.getItemBySlot(equipmentslot);
            float f = this.getEquipmentDropChance(equipmentslot);
            boolean flag = f > 1.0F;
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) &&
                    (pRecentlyHit || flag) && Math.max(ThreadLocalRandom.current()
                    .nextFloat() - (float)pLooting * 0.01F, 0.0F) < f) {
                if (!flag && itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getMaxDamage() - ThreadLocalRandom.current().nextInt(1
                            + ThreadLocalRandom.current().nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
                }
                this.spawnAtLocation(itemstack);
                this.setItemSlot(equipmentslot, ItemStack.EMPTY);
            }
        }
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty)
    {
        float f = pRandom.nextFloat();
        if (f > 0.75F) {
            return;
        }
        this.setItemSlot(EquipmentSlot.HEAD, (pRandom.nextBoolean() ? Items.GOLDEN_HELMET
                : Items.IRON_HELMET).getDefaultInstance());
        f = pRandom.nextFloat();
        if (f > 0.4F) {
            return;
        }
        this.setItemSlot(EquipmentSlot.FEET, Items.IRON_BOOTS.getDefaultInstance());
        f = pRandom.nextFloat();
        if (f > 0.1F) {
            return;
        }
        this.setItemSlot(EquipmentSlot.LEGS, Items.IRON_LEGGINGS.getDefaultInstance());
        f = pRandom.nextFloat();
        if (f > 0.05F) {
            return;
        }
        this.setItemSlot(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE.getDefaultInstance());
    }

    protected OwnableMob getSummoned()
    {
        return NoixmodAPIEntities.SKELETON_SERVANT.get().create(this.level());
    }

    public ApiPose getPoses()
    {
        if (this.isCastingSpell()) return ApiPose.SPELL_CASTING;
        return ApiPose.NATURAL;
    }

    public SoundEvent getCastSound()
    {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public APISpells.APISpell getSpellType()
    {
        return this.entityData.get(DATA_SPELL);
    }

    public void setSpellType(APISpells.APISpell apiSpell)
    {
        this.entityData.set(DATA_SPELL, apiSpell);
    }

    public int getSpellTick()
    {
        return this.entityData.get(DATA_SPELL_TICK);
    }

    public void setSpellTick(int i)
    {
        this.entityData.set(DATA_SPELL_TICK, i);
    }

    public boolean isCastingSpell()
    {
        return this.getSpellTick() > 0;
    }
    public MobType getMobType()
    {
        return MobType.UNDEAD;
    }
    protected SoundEvent getAmbientSound(){return SoundEvents.SKELETON_AMBIENT;}
    protected SoundEvent getHurtSound(DamageSource pDamageSource){return SoundEvents.SKELETON_HURT;}
    protected SoundEvent getDeathSound(){return SoundEvents.SKELETON_DEATH;}
    public void setTargets(int cooldown){}

    protected void populateDefaultItems()
    {
        this.setItemInHand(InteractionHand.MAIN_HAND, NoixmodAPIItems.BONE_STAFF.get().getDefaultInstance());
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 52.0D);
    }

    static {
        DATA_SPELL_TICK = SynchedEntityData.defineId(BoneSpellcaster.class, EntityDataSerializers.INT);
        DATA_SPELL = SynchedEntityData.defineId(BoneSpellcaster.class, ApiEntityDataSerializers.API_SPELL);
    }

    protected static class SummonSpellGoal extends AbstractUseSpellGoal {
        protected final BoneSpellcaster spellcaster;
        public SummonSpellGoal(SpellCasterMob finder)
        {
            super(finder);
            this.spellcaster = (BoneSpellcaster)finder;
        }

        protected void castSpell()
        {
            for (int i = 0;i < 1 + java.util.concurrent.ThreadLocalRandom.current().nextInt(2);i++) {
                var summoned = this.spellcaster.getSummoned();
                summoned.setHostile(true);
                this.spellcaster.ownerSummon.integerSummon(summoned, 3);
                summoned.spawnAnim();
            }
        }

        protected int getCastingTime()
        {
            return 50;
        }

        protected int getCastingInterval()
        {
            return 460;
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected APISpells.APISpell getSpell()
        {
            return APISpells.APISpell.RANGE;
        }
    }

    protected static class AttackSpellGoal extends AbstractUseSpellGoal
    {
        protected final BoneSpellcaster spellcaster;
        public AttackSpellGoal(SpellCasterMob finder)
        {
            super(finder);
            this.spellcaster = (BoneSpellcaster)finder;
        }

        public boolean canUse()
        {
            if (!checkTarget() || !this.spellcaster.closerThan(this.spellcaster.getTarget(), 4.0D)) {
                return false;
            }
            return !this.spellcaster.isCastingSpell() && this.spellcaster.tickCount >= this.nextAttackTickCount;
        }

        protected void castSpell()
        {
            Spells.GROUND.get().castSpell(this.spellcaster.serverLevel(), this.spellcaster);
        }

        protected int getCastingTime()
        {
            return 30;
        }

        protected int getCastingInterval()
        {
            return 100;
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected APISpells.APISpell getSpell()
        {
            return APISpells.APISpell.ATTACK;
        }
    }
}
