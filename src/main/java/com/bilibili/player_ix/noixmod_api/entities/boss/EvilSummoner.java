
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.DamageEntity;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class EvilSummoner
extends SpellcasterNihilist
implements InventoryCarrier, ApiNihilisticBoss {
    private static final float DAMAGE_CAPE = 17f;
    private int hurtCooldown;
    private final ServerBossEvent bossInfo;
    private final SimpleContainer inventory = new SimpleContainer();
    public EvilSummoner(EntityType<EvilSummoner> type, Level world) {
        super(type, world);
        this.bossInfo = new ServerBossEvent(this.getDisplayName(),
                BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new HealSpellGoal());
        this.goalSelector.addGoal(3, new SummonSpellGoal());
        this.goalSelector.addGoal(3, new RandomSummonGoal());
        this.goalSelector.addGoal(4, new AttackSpellGoal());
        this.goalSelector.addGoal(4, new AttackUpSpellGoal());
        this.goalSelector.addGoal(4, new StealSpellGoal());
        OwnableMob.addBehaviorGoals(this, 6, 0.8, 20F, true, true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class));
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            if (this.isCastingSpell()) {
                this.level().addParticle(NoixmodAPIParticleTypes.RED_SKULL.get(), this.getRandomX(1),
                        this.getRandomY(), this.getRandomZ(1), 0, 0, 0);
            }
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.hurtCooldown > 0) {
            this.hurtCooldown--;
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    protected InteractionResult mobInteract(Player p_21472_, InteractionHand p_21473_) {
        /*if (!this.isActive()) {
            p_21472_.openMenu(new SimpleMenuProvider())

        }*/
        return super.mobInteract(p_21472_, p_21473_);
    }

    protected void onEffectAdded(MobEffectInstance instance, @Nullable Entity pEntity) {
    }

    public boolean canBeAffected(MobEffectInstance p_21197_) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.CULTIST_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return NoixmodAPISounds.CULTIST_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return NoixmodAPISounds.CULTIST_DEATH.get();
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void startSeenByPlayer(ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        this.bossInfo.addPlayer(p_20119_);
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        this.bossInfo.removePlayer(p_20174_);
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        pAmount = Math.min(DAMAGE_CAPE, pAmount);
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        p_21241_ = Math.min(p_21241_, DAMAGE_CAPE);
        super.actuallyHurt(p_21240_, p_21241_);
    }

    public void setHealth(float p_21154_) {
        float health = this.getHealth();
        float delta = p_21154_ - health;
        if (delta < 0) {
            if (this.hurtCooldown > 0) {
                return;
            }
            this.hurtCooldown = 10;
            if (delta < -DAMAGE_CAPE) {
                p_21154_ = health - 17f;
            }
        }
        super.setHealth(p_21154_);
    }

    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        return super.killedEntity(p_216988_, p_216989_);
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        Entity entity = p_21192_.getEntity();
        int i = ForgeHooks.getLootingLevel(this, entity, p_21192_);
        this.captureDrops(new ArrayList<>());
        boolean flag = this.lastHurtByPlayerTime > 0;
        if (this.shouldDropLoot() && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFromLootTable(p_21192_, flag);
            this.dropCustomDeathLoot(p_21192_, i, flag);
        }
        this.dropOrGiveBackItems();
        this.dropExperience();
        @SuppressWarnings("ALL")
        Collection<ItemEntity> drops = this.captureDrops(null);
        if (!ForgeHooks.onLivingDrops(this, p_21192_, drops, i, this.lastHurtByPlayerTime > 0)) {
            drops.forEach((e) -> this.level().addFreshEntity(e));
        }
    }

    private void dropOrGiveBackItems() {
        LivingEntity living = this.getTarget();
        if (living instanceof Player carrier) {
            for (int i = 0; i < this.getInventory().getContainerSize(); i++) {
                carrier.getInventory().add(this.getInventory().getItem(i));
            }
        } else {
            for (int j = 0; j < this.getInventory().getContainerSize(); j++) {
                ItemEntity entity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getInventory().getItem(j));
                entity.setUnlimitedLifetime();
                this.level().addFreshEntity(entity);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(3, 0.4, 0.8)
                .add(Attributes.ARMOR, 4).add(Attributes.MAX_HEALTH, 349)
                .add(Attributes.FOLLOW_RANGE, 120);
    }

    private class SummonSpellGoal extends UseSpellGoalA {

        protected void castSpell() {
            if (EvilSummoner.this.level() instanceof ServerLevel level) {
                ISpell spell = Spells.NETHER_SOUL.get();
                spell.castSpell(level, EvilSummoner.this);
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }
    }

    private class AttackSpellGoal extends UseSpellGoalA {

        protected void castSpell() {
            if (!EvilSummoner.this.level().isClientSide) {
                LivingEntity target = EvilSummoner.this.getTarget();
                if (target != null) {
                    ServerLevel level = (ServerLevel) EvilSummoner.this.level();
                    DamageEntity entity = new DamageEntity(NoixmodAPIEntities.DAMAGE_ENTITY.get(), level);
                    entity.damage = 6f;
                    entity.source = damageSources().indirectMagic(EvilSummoner.this,
                            EvilSummoner.this);
                    entity.radius = 2f;
                    entity.dieParticles(NoixmodAPIParticleTypes.BLOOD_SPELL.get());
                    entity.setOwner(EvilSummoner.this);
                    entity.options = NoixmodAPIParticleTypes.BLOOD_SPELL.get();
                    entity.moveTo(target.blockPosition(), 0, 0);
                    level.addFreshEntity(entity);
                }
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 120;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.ATTACK;
        }
    }

    private class RandomSummonGoal extends UseSpellGoalA {

        protected void castSpell() {
            if (!level().isClientSide) {
                for (int i = 0;i < 7;i++) {
                    OwnableMob ownableMob = this.getSummon(serverLevel());
                    if (ownableMob != null) {
                        EvilSummoner.this.getSummon().integerSummon(ownableMob, 4);
                    }
                }
            }
        }

        @Nullable
        private OwnableMob getSummon(ServerLevel serverLevel) {
            Random util = getRandomUtil();
            int i = util.nextInt(8);
            switch (i) {
                case 0 -> {
                    return NoixmodAPIEntities.ENDER_MAN_SERVANT.get().create(serverLevel);
                }
                case 1 -> {
                    return NoixmodAPIEntities.GOLEM.get().create(serverLevel);
                }
                case 2 -> {
                    return NoixmodAPIEntities.VEX_SERVANT.get().create(serverLevel);
                }
                case 3 -> {
                    return NoixmodAPIEntities.GIRL_GHOST.get().create(serverLevel);
                }
                case 4 -> {
                    return NoixmodAPIEntities.NIHILISTIC_GHAST.get().create(serverLevel);
                }
                case 5 -> {
                    return NoixmodAPIEntities.HEALING.get().create(serverLevel);
                }
                case 6 -> {
                    return NoixmodAPIEntities.MAGICAL_CLONE.get().create(serverLevel);
                }
                default -> {
                    return NoixmodAPIEntities.WIND_ZOMBIE.get().create(serverLevel);
                }
            }
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.UNKNOWN;
        }
    }

    private class StealSpellGoal extends UseSpellGoalA {

        protected void castSpell() {
            LivingEntity target = EvilSummoner.this.getTarget();
            if (target != null) {
                if (target instanceof Player carrier) {
                    Inventory container = carrier.getInventory();
                    for (int i = 0; i < container.getContainerSize(); ++i) {
                        if (this.canSteal(container.getItem(i))) {
                            EvilSummoner.this.inventory.addItem(container.getItem(i));
                            container.removeItem(i, container.getMaxStackSize());
                        }
                    }
                } else {
                    for (ItemStack stack : target.getAllSlots()) {
                        if (stack != null) {
                            EvilSummoner.this.getInventory().addItem(stack);
                        }
                    }
                }
            }
        }

        public boolean canUse() {
            return super.canUse() && EvilSummoner.this.level().random.nextInt(9) ==0;
        }

        private boolean canSteal(ItemStack stack) {
            return stack.is(Tags.Items.ORES);
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 1500;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }

    private class HealSpellGoal extends UseSpellGoalA {

        protected void castSpell() {
            heal(10f);
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 800;
        }

        protected boolean needTarget() {
            return false;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.REGEN;
        }

        public boolean canUse() {
            if (getHealth() > getMaxHealth() -10f) {
                return false;
            }
            return super.canUse();
        }
    }

    private class AttackUpSpellGoal extends UseSpellGoalA {

        protected void castSpell() {
            if (!level().isClientSide) {
                LivingEntity target = getTarget();
                if (target != null) {
                    ServerLevel level = (ServerLevel)level();
                    List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class,
                            target.getBoundingBox().inflate(2, 0.2, 2),
                            living -> MobUtils.canHurt(living, EvilSummoner.this));
                    if (!list.isEmpty()) {
                        for (LivingEntity living : list) {
                            heal(3f);
                            living.hurt(damageSources().starve(), 10);
                        }
                    }
                    ParticleUtil.sendParticles(level, ParticleTypes.LARGE_SMOKE, target.position(),
                            25, 1, 0, 1, random.nextGaussian() * 0.2);
                }
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 180;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }
}
