
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.*;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.github.NineAbyss9.ix_api.util.ResourceLocations;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.client.particle.CircleParticleOption;
import com.bilibili.player_ix.noixmod_api.compat.bo.BlueOceansCompat;
import com.bilibili.player_ix.noixmod_api.compat.goety.GoetyCompat;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIAttributesConfig;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.ApostleAI;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.HorrorLookAtEntityGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.nihilist.Golem;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.ArrowRain;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticArrowRain;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.bilibili.player_ix.noixmod_api.entities.projectile.PowerEntity;
import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.NihilisticArrow;
import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonApostle;
import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonEntity;
import com.bilibili.player_ix.noixmod_api.entities.servant.WrongedSoul;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.*;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import com.bilibili.player_ix.noixmod_api.magic.nether.LavaTrapSpell;
import com.bilibili.player_ix.noixmod_api.register.*;
import com.bilibili.player_ix.noixmod_api.server.ApiBossEvent;
import com.bilibili.player_ix.noixmod_api.util.EntitiesFinder;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.NineAbyss9.util.ValueHolder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

/**
 * The follower of {@link NihilisticLord}.Fights with {@link BowItem}
 * @author Player_IX
 * */
public class Apostle
extends SpellcasterNihilist
implements ApiRangedAttackMob, Ownable, ApiTargeting
{
    protected ApiBossEvent bossEvent;
    private static final EntityDataAccessor<Integer> DATA_TARGET_ID;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID;
    private static final EntityDataAccessor<Boolean> DATA_SETTING_SECOND;
    protected static final EntityDataAccessor<Byte> DATA_PHASE;
    protected static final EntityDataAccessor<Integer> DATA_APOSTLE_SPELL;
    /// How long this apostle has been unable to recover health
    private static final EntityDataAccessor<Integer> DATA_CANCEL_REGEN_TIME;
    private static final EntityDataAccessor<Integer> DATA_HURT_COOLDOWN;
    protected final boolean inEnd;
    private DamageSource causeKilled;
    public float spin;
    private int hurtCount;
    private int cooldown;
    private int fireCooldown;
    private int statueCooldown;
    private long statueSpeedTime;
    private int trueDeathTime;
    private float arrowDamage;
    private float spellPower;
    public int pressureTicks;
    private boolean fast;
    private double dangerDouble;
    public long invTime;
    private int afraidTick;
    private int clientSideIllusionTicks;
    protected long arrowRainCooldown;
    protected int lightningCooldown = 100;
    @Nullable
    protected LivingEntity apostleTarget;
    protected long spreadFireballTicks;
    protected long escapeTime;
    protected long lostTargetTime;
    protected int tickSummon = 100;
    protected int cancelHealTick;
    protected int lavaCooldown;
    protected int serverHurtCooldown;
    protected OwnableData ownableData;
    @Nullable
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;
    private final Vec3[][] clientSideIllusionOffsets;
    protected final ApiBossEvent horrorEvent;
    protected final ApostleAI ai = new ApostleAI(this);
    public final OwnerSummon ownerSummon = new OwnerSummon(this);
    protected static final float[] DIE_HEALTH = {0.99999901063212F, 19.999213345534F};
    protected static final AttributeModifier CASTING_SPEED;
    protected static final AttributeModifier FAST_SPEED;
    protected static final AttributeModifier STATUE_COOLDOWN_SPEED;
    protected static final AttributeModifier ZERO_SPEED;

    public Apostle(EntityType<? extends Apostle> pEntityType, Level world) {
        super(pEntityType, world);
        bossEvent = new ApiBossEvent(this, Component.translatable("entity.noixmodapi.apostle")
                .withStyle(ChatFormatting.DARK_PURPLE), BossEvent.BossBarColor.PURPLE,
                false, true);
        horrorEvent = new ApiBossEvent(this,
                Component.literal("Apostle").withStyle(ChatFormatting.OBFUSCATED,
                        ChatFormatting.DARK_RED), BossEvent.BossBarColor.RED, true, true);
        this.clientSideIllusionOffsets = new Vec3[2][4];
        for (int i = 0; i < 4; ++i) {
            this.clientSideIllusionOffsets[0][i] = Vec3.ZERO;
            this.clientSideIllusionOffsets[1][i] = Vec3.ZERO;
        }
        this.ownableData = new OwnableData(this);
        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStacks.of(Items.BOW));
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 1.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 1.0f);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.spellPower = this.getMaxSpellPower();
        this.setPersistenceRequired();
        this.setFastSpeed();
        this.inEnd = this.isInEnd();
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.APOSTLE.get();
    }

    public Vec3[] getIllusionOffsets(float ticks) {
        if (this.clientSideIllusionTicks <= 0) {
            return this.clientSideIllusionOffsets[1];
        } else {
            double d0 = this.clientSideIllusionTicks - ticks / 3.0F;
            d0 = Math.pow(d0, 0.25D);
            Vec3[] avec3 = new Vec3[4];
            for (int i = 0; i < 4; ++i) {
                avec3[i] = this.clientSideIllusionOffsets[1][i].scale(1.0D - d0).add(this
                        .clientSideIllusionOffsets[0][i].scale(d0));
            }
            return avec3;
        }
    }

    /**
     * @return UUID of Apostle's Owner
     */
    @Nullable
    public UUID getOwnerUUID() {
        return null;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
    }

    /**
     * @return {@link ServerLevel} of apostle
     * @throws ClassCastException if the {@link  #level()} of apostle is {@link ClientLevel}
     * @see ClientLevel
     * @see ServerLevel
     * @see Level
     */
    public ServerLevel serverLevel() throws ClassCastException {
        return (ServerLevel)level();
    }

    /**
     * @return the {@link ClientLevel} of apostle
     * @throws ClassCastException if the {@link  #level()} of apostle is {@link ServerLevel}
     * @see ClientLevel
     * @see ServerLevel
     * @see Level
     */
    public ClientLevel clientLevel() throws ClassCastException {
        return (ClientLevel)level();
    }

    @Nullable
    public LivingEntity getApostleTarget() {
        return this.apostleTarget;
    }

    public OwnableData getOwnableData() {
        return ownableData;
    }

    public List<LivingEntity> getApostleTargets() {
        return this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32),
                living -> this.getTarget() == living || (living instanceof Mob mob && mob.getTarget() == this));
    }

    public void setApostleTarget(@Nullable LivingEntity entity) {
        this.apostleTarget = entity;
    }

    @Nullable
    public UUID getTargetUuid() {
        return this.entityData.get(DATA_TARGET_UUID).orElse(null);
    }

    public void setTargetUuid(@Nullable UUID uuid) {
        this.entityData.set(DATA_TARGET_UUID, Optional.ofNullable(uuid));
    }

    public int getTargetId() {
        return this.entityData.get(DATA_TARGET_ID);
    }

    public void setTargetId(int id) {
        this.entityData.set(DATA_TARGET_ID, id);
    }

    public long getSpreadFireballTicks() {
        return spreadFireballTicks;
    }

    @ServerOnly
    public void summonWither()
    {
        NihilisticWither wither = new NihilisticWither(NoixmodAPIEntities.NIHILISTIC_WITHER.get(), this.level());
        wither.moveTo(this.blockPosition(), 0, 0);
        wither.setOwner(OwnableMob.ownerOrThis(this));
        wither.finalizeSpawn(this.serverLevel(), this.serverLevel().getCurrentDifficultyAt(this.blockPosition()),
                MobSpawnType.MOB_SUMMONED);
        this.level().addFreshEntity(wither);
        ParticleUtil.sendParticles(this.serverLevel(), ParticleTypes.LARGE_SMOKE, wither.position(),
                30, 1.5, 1.5, 1.5, 0);
    }

    public void setSpreadingFireball() {
        this.spreadFireballTicks = Maths.toTick(10);
    }

    public void resetSpreadFireball() {
        this.spreadFireballTicks = 0;
    }

    public void handleAfraid() {
        this.afraidTick = 120;
        this.arrowDamage -= 1F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SetSecondPhaseGoal(this));
        this.goalSelector.addGoal(2, new ApostleBowAttackGoal(this));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new SummonArrowsSpellGoal(this));
        this.goalSelector.addGoal(3, new CloneSpellGoal(this));
        this.goalSelector.addGoal(3, new ShadowSpellGoal(this));
        this.goalSelector.addGoal(3, new TrialSpellGoal(this));
        this.goalSelector.addGoal(3, new ShootFireballGoal(this));
        this.goalSelector.addGoal(3, new SummonStatueSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonSoulGoal(this));
        this.goalSelector.addGoal(3, new RoarSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonPowerEntitySpellGoal(this));
        this.goalSelector.addGoal(3, new SpreadFireballGoal(this));
        this.goalSelector.addGoal(3, new SummonArrowRainSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonStaySoulGoal(this));
        this.goalSelector.addGoal(3, new RangedSummonSpellGoal(this));
        //this.goalSelector.addGoal(3, new ArmoredZombieSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonServantsSpellGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(5, new ApostleLookAtEntityGoal(this));
        this.goalSelector.addGoal(5, new ApostleRandomLookGoal(this));
        this.goalSelector.addGoal(5, new ApostleRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ApostleAttackPlayerGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                AbstractGolem.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, false, living -> living instanceof ApiVillager));
    }

    public int getCancelHealTick() {
        return this.cancelHealTick;
    }

    public void setCancelHealTick(int i) {
        this.cancelHealTick = i;
    }

    public int getCancelRegenTick() {
        return this.entityData.get(DATA_CANCEL_REGEN_TIME);
    }

    public void setCancelRegenTick(int tick) {
        this.entityData.set(DATA_CANCEL_REGEN_TIME, tick);
    }

    public boolean wouldHaveOwner() {
        return false;
    }

    public boolean isAlliedTo(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this) {
            return true;
        }
        if (entity instanceof Ownable ownable) {
            return this.isAlliedTo(ownable.getOwner());
        }
        return super.isAlliedTo(entity);
    }

    public void rideTick() {
        this.stopRiding();
    }

    public boolean isNoAi() {
        return false;
    }

    public void setNoAi(boolean p_21558_) {
    }

    //Damage cap for apostle
    public void setHealth(float amount) {
        if (Float.compare(amount, DIE_HEALTH[0]) == 0 || Float.compare(amount, DIE_HEALTH[1]) == 0) {
            super.setHealth(amount);
            return;
        }
        float delta = amount - this.getHealth();
        if (delta < 0) {
            if (this.isSettingSecondPhase()) {
                return;
            }
            if (this.serverHurtCooldown > 0) {
                return;
            }
            this.serverHurtCooldown = 20;
            float f = NoixmodAPIAttributesConfig.apostleDamageCap.get().floatValue();
            if (delta < -f) {
                amount = this.getHealth() - f;
            }
        }
        super.setHealth(amount);
    }

    public int getHurtCooldown() {
        return this.entityData.get(DATA_HURT_COOLDOWN);
    }

    public void setHurtCooldown(int hurtCooldown) {
        this.entityData.set(DATA_HURT_COOLDOWN, hurtCooldown);
    }

    public boolean isBaby() {
        return this.entityData.get(DATA_BABY);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("SpellPower", this.getSpellPower());
        tag.putFloat("ArrowDamage", this.getArrowDamage());
        tag.putBoolean("settingSecondPhase", this.isSettingSecondPhase());
        tag.putByte("Phase", this.getPhase());
        this.addOwnableAdditionalSaveData(tag);
        this.ownableData.addOwnableAdditionalSaveData(tag);
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.spellPower = tag.getFloat("SpellPower");
        this.arrowDamage = tag.getFloat("ArrowDamage");
        if (tag.contains("Health")) {
            tag.remove("Health");
        }
        if (tag.contains("settingSecondPhase")) {
            this.setSettingSecondPhase(tag.getBoolean("settingSecondPhase"));
        }
        if (tag.contains("Phase")) {
            this.setPhase(tag.getByte("Phase"));
        }
        this.readOwnableAdditionalSaveData(tag);
        this.ownableData.readOwnableAdditionalSaveData(tag);
        super.readAdditionalSaveData(tag);
    }

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.ownableData.nextFlag(player, hand)) {
            this.ownableData.nextFlag();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    public boolean isAttackable() {
        return this.getHurtCooldown() <= 0 && !this.isSettingSecondPhase();
    }

    public MoveControl getMoveControl() {
        return this.moveControl;
    }

    public boolean isShadow() {
        return false;
    }

    public boolean isClone() {
        return false;
    }

    public boolean isBoss() {
        return false;
    }

    public void startSeenByPlayer(ServerPlayer p_20119_)
    {
        super.startSeenByPlayer(p_20119_);
        if (this.isClone()) {
            return;
        }
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            this.horrorEvent.addPlayer(p_20119_);
        } else {
            this.bossEvent.addPlayer(p_20119_);
        }
    }

    public void stopSeenByPlayer(ServerPlayer p_20174_)
    {
        super.stopSeenByPlayer(p_20174_);
        if (this.isClone()) {
            return;
        }
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            this.horrorEvent.removePlayer(p_20174_);
        } else {
            this.bossEvent.removePlayer(p_20174_);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getCancelHealTick() > 0) {
            this.cancelHealTick--;
        }
        if (this.getCancelRegenTick() > 0) {
            this.setCancelRegenTick(this.getCancelRegenTick() - 1);
        }
        this.handleBossEvent();
        if (this.lavaCooldown > 0) {
            this.lavaCooldown--;
        }
        if (this.invTime > 0) {
            this.invTime--;
        }
        if (this.statueSpeedTime > 0) {
            this.statueSpeedTime--;
        }
        if (this.arrowRainCooldown > 0) {
            this.arrowRainCooldown--;
        }
        if (this.tickSummon > 0) {
            this.tickSummon--;
        }
        if (this.spreadFireballTicks > 0) {
            this.spreadFireballTicks--;
        }
        if (this.escapeTime > 0) {
            this.escapeTime--;
        }
        if (this.lostTargetTime > 0) {
            this.lostTargetTime--;
        }
        if (this.serverHurtCooldown > 0) {
            this.serverHurtCooldown--;
        }
        if (this.afraidTick > 0) {
            this.afraidTick--;
        }
    }

    protected void handleBossEvent()
    {
        if (!this.isClone()) {
            if (NoixmodAPIMainConfig.HorrorMode.get()) {
                this.horrorEvent.setProgress(this.getHealth() / this.getMaxHealth());
            } else {
                if (this.tickCount % 5 == 0) {
                    this.bossEvent.update();
                }
                this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            }
        }
    }

    public void setInvTime(long time) {
        this.invTime = time;
    }

    public static AttributeSupplier.Builder createBaseAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.FOLLOW_RANGE, 160)
                .add(Attributes.ARMOR, NoixmodAPIAttributesConfig.apostleArmor.get())
                .add(Attributes.ARMOR_TOUGHNESS, NoixmodAPIAttributesConfig.apostleArmorToughness.get())
                .add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Apostle.createBaseAttributes()
                .add(Attributes.MAX_HEALTH, NoixmodAPIAttributesConfig.apostleMaxHealth.get());
    }

    public boolean isFullyFrozen() {
        return false;
    }

    public void setTicksFrozen(int p_146918_) {
    }

    public LivingEntity self() {
        return super.self();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean isImmobile() {
        return this.isSettingSecondPhase() || super.isImmobile();
    }

    public boolean isPickable() {
        return super.isPickable() && !this.isSettingSecondPhase();
    }

    public float getSpellPower() {
        return this.spellPower;
    }

    public void setSpellPower(float f) {
        this.spellPower = f;
    }

    public void setSettingSecondPhase(boolean b) {
        this.entityData.set(DATA_SETTING_SECOND, b);
    }

    public boolean isFast() {
        return this.fast;
    }

    public int getStatueCooldown() {
        return this.statueCooldown;
    }

    public void setStatueCooldown() {
        this.statueCooldown = Maths.toTick(50);
        int i = this.isSecondPhase() ? 15 : 30;
        this.statueSpeedTime = Maths.toTick(i);
    }

    public double getDangerDouble() {
        return this.dangerDouble;
    }

    public void setDangerDouble() {
        this.dangerDouble = 8;
    }

    public void setFastSpeed() {
        if (this.getRandomUtil().nextFloat() <= 0.009f) {
            this.setDangerDouble();
            this.fast = true;
        }
    }

    public void setTraits(int trait) {
        switch (trait) {
            case 0 -> this.arrowDamage += 0.25F;
            case 1 -> this.resetCooldown();
            case truth -> {
                this.spellPower += 90.0F;
                this.arrowDamage += 0.5F;
            }
            default -> this.spellPower += 20.0F;
        }
    }

    public boolean isServant() {
        return this.getOwner() != null;
    }

    public final boolean removeWhenFarAway(double p_33073_) {
        return false;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TARGET_ID, 0);
        this.entityData.define(DATA_TARGET_UUID, Optional.empty());
        this.entityData.define(DATA_HURT_COOLDOWN, 0);
        this.entityData.define(DATA_CANCEL_REGEN_TIME, 0);
        this.entityData.define(DATA_PHASE, Maths.ONE_BYTE);
        this.entityData.define(DATA_SETTING_SECOND, false);
        this.entityData.define(DATA_APOSTLE_SPELL, 0);
    }

    public byte getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(byte b) {
        byte phase = (byte)Mth.clamp(b, 1, 2);
        this.entityData.set(DATA_PHASE, phase);
    }

    public boolean isSettingSecondPhase() {
        return this.entityData.get(DATA_SETTING_SECOND);
    }

    public boolean isSecondPhase() {
        return this.getPhase() == Maths.TWO_BYTE;
    }

    public OwnerSummon getSummon() {
        return this.ownerSummon;
    }

    public void summonShadow() {
        if (!this.level().isClientSide) {
            ApostleShadow shadow = new ApostleShadow(NoixmodAPIEntities.APOSTLE_SHADOW.get(), this.level());
            shadow.setArrowDamage(this.getArrowDamage());
            shadow.setOwner(OwnableMob.ownerOrThis(this));
            shadow.setTarget(this.getTarget());
            this.getSummon().integerSummon(shadow, 6);
        }
    }

    public void summonSoul() {
        if (!this.level().isClientSide) {
            WrongedSoul soul = new WrongedSoul(NoixmodAPIEntities.WRONGED_SOUL.get(), this.serverLevel());
            BlockPos.MutableBlockPos $$2 = this.blockPosition().offset(Maths.randomInteger(2), 0,
                    Maths.randomInteger(2)).below().mutable();
            soul.moveTo($$2, 0, 0);
            soul.setTarget(this.getTarget());
            soul.setOwner(this);
            soul.finalizeSpawn(this.serverLevel(), this.serverLevel().getCurrentDifficultyAt(this.blockPosition()),
                    MobSpawnType.MOB_SUMMONED);
            this.serverLevel().addFreshEntity(soul);
            this.serverLevel().sendParticles(ParticleTypes.LARGE_SMOKE, soul.getX(), soul.getY() - 0.15,
                    soul.getZ(), 10, 1, 0, 1, 0.05);
        }
    }

    public float getMaxSpellPower() {
        if (this.isEasy()) {
            return 1400;
        } else if (this.isNormal()) {
            return 1600;
        } else {
            return 1800;
        }
    }

    public boolean reallyDangerous() {
        return this.getHealth() <= this.getMaxHealth() / 4;
    }

    public void chaseTeleport() {
        if (!this.level().isClientSide) {
            LivingEntity lie = this.getTarget();
            if (this.getRandom().nextFloat() <= 0.005 && !this.isShadow() && !this.isNihilistic()) {
                this.summonShadow();
            }
            if (lie != null) {
                double x = lie.getX();
                double y = lie.getY();
                double z = lie.getZ();
                for (int i = 0;i < 5;++i) {
                    double randomX = x + (this.getRandom().nextDouble() - 0.5) * 24;
                    double randomZ = z + (this.getRandom().nextDouble() - 0.5) * 24;
                    if (this.randomTeleport(randomX, y, randomZ, false)) {
                        double dis = 0.5;
                        this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.5f);
                        this.serverLevel().sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 1,
                                this.getZ(), 30, dis, dis, dis, 0.1);
                        break;
                    }
                }
            }
        }
    }

    public void teleport() {
        if (!this.level().isClientSide) {
            if (this.getRandom().nextFloat() <= 0.005 && !this.isShadow() && !this.isNihilistic()) {
                this.summonShadow();
            }
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            for (int i = 0;i < 99;++i) {
                double randomX = x + (this.getRandom().nextDouble() - 0.5) * 24;
                double ry = y + (this.getRandom().nextDouble() - 0.5) * 5;
                double randomZ = z + (this.getRandom().nextDouble() - 0.5) * 24;
                if (this.randomTeleport(randomX, ry, randomZ, false)) {
                    double dis = 0.5;
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.5f);
                    this.serverLevel().sendParticles(ParticleTypes.SMOKE, x, y + 1, z, 30,
                            dis, dis, dis, 0.1);
                    if (this.isHard() && this.isSecondPhase() && !this.isNihilistic()) {
                        this.summonSoul();
                    }
                    break;
                }
            }
        }
    }

    public int getSpell() {
        return this.entityData.get(DATA_APOSTLE_SPELL);
    }

    public void setApostleSpell(int i) {
        this.entityData.set(DATA_APOSTLE_SPELL, i);
    }

    public ParticleOptions getApostleSpellParticle() {
        return switch (this.getSpell()) {
            case (-1) -> ParticleTypes.SOUL;
            case (2) -> ParticleTypes.SOUL_FIRE_FLAME;
            case (3) -> NoixmodAPIParticleTypes.DARK_SPELL.get();
            case (4) -> ParticleTypes.WITCH;
            case (5) -> ParticleTypes.REVERSE_PORTAL;
            case (6) -> ParticleTypes.SMOKE;
            case (truth) -> NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get();
            default -> ParticleTypes.ENTITY_EFFECT;
        };
    }

    protected ParticleOptions getSpellParticle() {
        return NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get();
    }

    public boolean isEasy() {
        return this.level().getDifficulty().equals(Difficulty.EASY);
    }

    public boolean isNormal() {
        return this.level().getDifficulty().equals(Difficulty.NORMAL);
    }

    public boolean isHard() {
        return this.level().getDifficulty().equals(Difficulty.HARD);
    }

    public boolean hardOrNormal() {
        return this.isNormal() || this.isHard();
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int cool) {
        this.cooldown = cool;
    }

    public void resetCooldown() {
        this.setCooldown(0);
    }

    public void summonServant() {
        if (!this.level().isClientSide) {
            NihilisticServant servant = new NihilisticServant(NoixmodAPIEntities.NIHILISTIC_SERVANT.get(),
                    this.serverLevel());
            servant.handleLifeTicks();
            SummonEntity entity = NoixmodAPIEntities.SUMMON_ENTITY.get().create(this.serverLevel());
            entity.entity(NoixmodAPIEntities.NIHILISTIC_SERVANT.get());
            entity.setDangerous(true);
            this.getSummon().integerSummon(entity, 7);
        }
    }

    public void summonServants() {
        if (!this.level().isClientSide) {
            if (this.isSecondPhase()) {
                this.getSummon().summonWithSummonEntity(NoixmodAPIEntities.NIHILISTIC_SERVANT.get(),
                        8, true);
            } else {
                this.getSummon().summonWithSummonEntity(NoixmodAPIEntities.GOLEM.get(), 8, true);
            }
        }
    }

    public static Predicate<Entity> ownerPredicate(LivingEntity lie) {
        return entity -> entity instanceof Ownable && entity != lie && !(entity instanceof Apostle)
                && !(entity instanceof IProjectile);
    }

    public void summonRangedServant() {
        int i = this.isHorror() ? 2 : 0;
        this.summonRangedServant(1 + i, 4 + i);
    }

    public void summonRangedServant(int ghastCount, int blazeCount)
    {
        LivingEntity lie = this.getTarget();
        if (lie != null) {
            if (java.util.concurrent.ThreadLocalRandom.current().nextBoolean()) {
                for (int i = 0;i < (this.getRandomUtil().nextBoolean() ? ghastCount : ghastCount + 1);++i) {
                    ServerLevel world = this.serverLevel();
                    int j = Maths.randomInteger(6);
                    int k = Maths.randomInteger(6);
                    BlockPos.MutableBlockPos pos = this.blockPosition().offset(k, 0, j).mutable();
                    SummonEntity entity = new SummonEntity(NoixmodAPIEntities.SUMMON_ENTITY.get(), world);
                    entity.entity(NoixmodAPIEntities.NIHILISTIC_GHAST.get());
                    entity.setDangerous(true);
                    entity.moveTo(pos, 0, 0);
                    entity.setOwner(this);
                    this.level().addFreshEntity(entity);
                }
            } else {
                for (int i = 0;i < (this.random.nextInt(blazeCount) + 1);++i) {
                    ServerLevel level = this.serverLevel();
                    int j = Maths.randomInteger(10);
                    int k = Maths.randomInteger(10);
                    BlockPos.MutableBlockPos $$2 = this.blockPosition().offset(k, 0, j).mutable();
                    SummonEntity entity = new SummonEntity(NoixmodAPIEntities.SUMMON_ENTITY.get(), level);
                    entity.entity(NoixmodAPIEntities.NIHILISTIC_BLAZE.get());
                    entity.setDangerous(false);
                    entity.setOwner(this);
                    entity.moveTo($$2, 0, 0);
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    public void summonEntity() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            NihilisticArrowRain rain = new NihilisticArrowRain(NoixmodAPIEntities.NIHILISTIC_ARROW_RAIN.get(),
                    this.level());
            rain.setOwner(this);
            rain.moveTo(target.blockPosition().offset(0, 15, 0), 0, 0);
            NihilisticArrow arrow = new NihilisticArrow(NoixmodAPIEntities.NIHILISTIC_ARROW.get(), this.level());
            arrow.setCritArrow(this.inEnd);
            arrow.setBaseDamage(this.getArrowDamage());
            arrow.setOwner(this);
            arrow.setEffectsFromItem(this.getMainHandItem());
            arrow.setEnchantmentEffectsFromEntity(this, 5f);
            rain.setRainArrow(arrow);
            this.level().addFreshEntity(rain);
        }
        if (!this.level().isClientSide) {
            if (!OwnerSummon.canSummonEntity(this.serverLevel(), this, 3, entity -> entity instanceof
                    ArrowRain)) {
                this.arrowRainCooldown = Maths.toTick(20);
            }
        }/*else {
            BlackHole hole = new BlackHole(NoixmodAPIEntities.BLACK_HOLE.get(), this.level());
            hole.setOwner(this);
            hole.moveTo(this.blockPosition(), 0, 0);
            this.level().addFreshEntity(hole);
            if (!this.level().isClientSide()) {
                if (!OwnerSummon.canSummonEntity(this.getServerLevel(), this, 3, entity -> entity instanceof BlackHole)) {
                    this.arrowRainCooldown = Maths.toTick(20);
                }
            }
        }
        */
    }

    public void quake() {
        MobUtils.rangeHurt(4, 0.2, 4, this, this.damageSources().indirectMagic(this,
                this), 4);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()
                .inflate(6, 0.2, 6), (living) -> MobUtils.canHurt(living, this));
        if (!list.isEmpty()) {
            for (LivingEntity lie : list) {
                double d = NoixmodAPIAttributes.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE, lie);
                lie.push(0, 1.3 - d, 0);
            }
        }
        this.playSound(SoundEvents.GENERIC_EXPLODE);
        this.makeGroundParticle();
    }

    public void roar() {
        ISpell spell = Spells.NIHILISTIC_ROAR.get();
        if (!this.level().isClientSide) {
            spell.castSpell(this.serverLevel(), this);
        }
    }

    public void summonStaySoul() {
        if (!this.level().isClientSide) {
            ServerLevel level = this.serverLevel();
            WrongedSoul soul = new WrongedSoul(NoixmodAPIEntities.WRONGED_SOUL.get(), level);
            int i = Maths.randomInteger(8);
            int j = Maths.randomInteger(8);
            BlockPos.MutableBlockPos pos = this.blockPosition().offset(i, 0, j).above().mutable();
            soul.setOwner(this.getOwner() == null ? this : this.getOwner());
            soul.setState(1);
            soul.stay = true;
            soul.setLifeTick(-this.random.nextInt(4));
            soul.moveTo(pos, 0, 0);
            soul.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED);
            level.addFreshEntity(soul);
            level.sendParticles(ParticleTypes.LARGE_SMOKE, soul.getX(), soul.getY(), soul.getZ(), 10,
                    0.5, 0, 0.5, 0.15);
            soul.playSound(SoundEvents.CAVE_VINES_PLACE);
        }
    }

    public void summonPower() {
        Level level = this.level();
        PowerEntity power = new PowerEntity(NoixmodAPIEntities.POWER_ENTITY.get(), level);
        power.setOwner(this);
        power.moveTo(this.blockPosition().offset(Maths.randomInt(2), 2, Maths.randomInt(2)),
                0, 0);
        level.addFreshEntity(power);
    }

    public void summonFireball()
    {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        OwnerSummon ownerSummon = this.getSummon();
        double d1 = ownerSummon.projectileDouble(target)[0];
        double d2 = ownerSummon.projectileDouble(target)[1];
        double d3 = ownerSummon.projectileDouble(target)[2];
        NihilisticFireball fireBall = new NihilisticFireball(this.level(), this, d1, d2, d3);
        fireBall.setPosRaw(fireBall.getX(), this.getY(0.5) + 0.5, fireBall.getZ());
        fireBall.setDamage((float)this.getArrowDamage());
        fireBall.setOwner(this);
        fireBall.setRadius(4D);
        this.level().addFreshEntity(fireBall);
    }

    @Nullable
    public LivingEntity getTarget() {
        if (this.level().isClientSide) {
            int id = this.getTargetId();
            Entity entity = this.level().getEntity(id);
            return (id <= -1 || !(entity instanceof LivingEntity)) ? null : (LivingEntity)entity;
        } else {
            return EntitiesFinder.getLivingEntity(this.level(), this.getTargetUuid());
        }
    }

    public boolean isAfraid() {
        return this.afraidTick > 0;
    }

    public boolean startRiding(Entity p_20330_) {
        return false;
    }

    public boolean startRiding(Entity p_21396_, boolean p_21397_) {
        return false;
    }

    @Nullable
    public Team getTeam() {
        LivingEntity living = this.getOwner();
        if (living != null && !this.areBothOwner(living)) {
            return living.getTeam();
        }
        return super.getTeam();
    }

    public boolean isInDanger() {
        if (this.getTarget() == null) {
            return false;
        } else {
            return this.closerThan(this.getTarget(), getDangerDouble());
        }
    }

    public void setSpin(float spin) {
        this.spin = spin;
    }

    public void tick() {
        if (!this.level().isClientSide)
        {
            this.setCastingSpeed();
            if (this.isBoss())
            {
                this.setApostleSpell();
                this.handleTitleEvents();
            }
        }
        super.tick();
        LivingEntity lie = this.getTarget();
        if (this.isInWater() || this.isInLava() || this.isInWall() || this.isInPowderSnow) {
            this.teleport();
        }
        if (this.lightningCooldown > 0) {
            this.lightningCooldown--;
        }
        if (this.spellPower < this.getMaxSpellPower()) {
            this.spellPower++;
        }
        if (this.getHurtCooldown() > 0) {
            this.setHurtCooldown(this.getHurtCooldown() - 1);
        }
        if (this.statueCooldown > 0) {
            this.statueCooldown--;
        }
        if (this.level().isClientSide) {
            if (this.isCastingSpell()) {
                double d = 0.5D - this.random.nextDouble();
                double di = this.random.nextGaussian() * 0.1;
                double d1 = this.random.nextGaussian() * 0.1;
                double d2 = this.random.nextGaussian() * 0.1;
                if (this.getApostleSpellParticle() == ParticleTypes.SOUL_FIRE_FLAME || this.getApostleSpellParticle()
                        == ParticleTypes.SOUL || this.getApostleSpellParticle() == ParticleTypes.REVERSE_PORTAL
                        || this.getApostleSpellParticle() == NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get()) {
                    this.clientLevel().addParticle(this.getApostleSpellParticle(), this.getX(), this.getY(), this.getZ(), di, d1, d2);
                } else {
                    this.clientLevel().addParticle(this.getApostleSpellParticle(), this.getX(), this.getY(), this.getZ(),
                            d, 0.2D, d);
                }
            }
            if (this.lightningCooldown <= 0 && java.util.concurrent.ThreadLocalRandom.current().nextFloat() <= 0.05F && this.isSecondPhase()
                    && this.clientLevel().getSkyFlashTime() <= 0 && this.isInOverworld()) {
                this.clientLevel().setSkyFlashTime(3);
                this.clientLevel().playLocalSound(this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER
                        , SoundSource.WEATHER, 2f, 1f, true);
                this.lightningCooldown = 600;
            }
        } else {
            if (this.getSpreadFireballTicks() > 0 && this.getSpreadFireballTicks() % 5 == 0) {
                NihilisticFireball ball = new NihilisticFireball(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(),
                        this.level());
                ball.moveTo(this.blockPosition().offset(Maths.randomInteger(10), 15,
                        Maths.randomInteger(10)), 0, 0);
                ball.setMoveDown();
                this.level().addFreshEntity(ball);
            }
            if (this.pressureTicks > 0) {
                this.pressureTicks--;
                if (lie != null && this.isInDanger()) {
                    lie.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 1));
                    lie.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 1));
                }
            }
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.fireCooldown > 0) {
                this.fireCooldown--;
            }
            if (this.escapeTime <= 0 && this.isHalfHealth()) {
                this.teleport();
                this.escapeTime = Maths.toTick(9);
            }
            if (lie != null) {
                boolean flag = this.getSensing().hasLineOfSight(lie);
                if (((this.distanceToSqr(lie) > Maths.square(45)) || !flag)
                        && !this.isSettingSecondPhase() && this.isAlive()) {
                    this.chaseTeleport();
                }
                if (!this.isNihilistic()) {
                    if (this.fireCooldown == 0 && this.isSecondPhase()) {
                        this.fireCooldown = Maths.toTick(12);
                        this.summonSoul();
                        this.arrowDamage++;
                    }
                    if (this.tickSummon == 0 && !this.isShadow()) {
                        this.tickSummon = Maths.toTick(45) + Maths.toTick(this.getRandom().nextInt(5));
                        this.summonServants();
                        this.summonServant();
                    }
                }
            }
        }
        if (this.isAlive()) {
            if (this.spin < Maths.CLOSER_PI) {
                this.setSpin(this.spin + (Maths.CLOSER_PI / 180));
            } else {
                this.setSpin(-Maths.CLOSER_PI);
            }
            if (this.isSettingSecondPhase() && !this.isSecondPhase()) {
                if (this.level().isClientSide) {
                    for (int i = 0;i < 20;++i) {
                        double d = this.random.nextGaussian() * 0.4;
                        double d1 = this.random.nextGaussian() * 0.4;
                        double d2 = this.random.nextGaussian() * 0.4;
                        this.clientLevel().addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, this.getX(),
                                this.getY() + 0.5, this.getZ(), d, d1, d2);
                    }
                } else {
                    this.getNavigation().stop();
                    this.heal(this.getMaxHealth() / 320.0F);
                    this.healSelf(this.getMaxHealth() / 160.0F);
                    if (this.getHealth() == this.getMaxHealth()) {
                        if (this.isBoss()) {
                            this.sendSystemMessage(ApostleBoss.horror("You have no way out."));
                        }
                        this.setPhase(Maths.TWO_BYTE);
                        this.setSettingSecondPhase(false);
                    }
                }
            }
        }
    }

    protected void spellTick() {
        APISpells.APISpell spellId = this.getSpellId() == APISpells.APISpell.NONE ? this.lastSpell : this.getSpellId();
        double $$1 = spellId.spellColor[0];
        double $$2 = spellId.spellColor[1];
        double $$3 = spellId.spellColor[2];
        float $$4 = this.yBodyRot * (Maths.CLOSER_PI / 180) + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
        float $$5 = Mth.cos($$4);
        float $$6 = Mth.sin($$4);
        if (this.isNihilistic()) {
            Vec3[] vec3s = this.getIllusionOffsets(this.tickCount);
            for (Vec3 vec3 : vec3s) {
                if (this.getMainArm() == HumanoidArm.RIGHT) {
                    this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), vec3.x
                            + $$5 * 0.6, vec3.y + 1.8, vec3.z + $$6 * 0.6, $$1, $$2, $$3);
                } else {
                    this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), vec3.x
                            - $$5 * 0.6, vec3.y + 1.8, vec3.z - $$6 * 0.6, $$1, $$2, $$3);
                }
            }
        }
        if (this.getMainArm() == HumanoidArm.RIGHT) {
            this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), this.getX()
                    + $$5 * 0.6, this.getY() + 1.8, this.getZ() + $$6 * 0.6, $$1, $$2, $$3);
        } else {
            this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_SPELL.get(), this.getX()
                    - $$5 * 0.6, this.getY() + 1.8, this.getZ() - $$6 * 0.6, $$1, $$2, $$3);
        }
    }

    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0, 0.0, 3.0);
    }

    public boolean attackable() {
        return !this.isSettingSecondPhase();
    }

    public boolean isInvulnerable() {
        if (this.isSettingSecondPhase()) {
            return true;
        }
        return super.isInvulnerable();
    }

    public int getTrueDeathTime() {
        return this.trueDeathTime;
    }

    public boolean isHalfHealth() {
        return MobUtils.isHalfHealth(this);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null) {
            if (GoetyCompat.goetyLoaded()) {
                return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocations.parse(
                        "goety:apostle_ambient"));
            }
            return NoixmodAPISounds.APOSTLE_IDLE.get();
        } else {
            return null;
        }
    }

    public boolean isInOverworld() {
        return this.level().dimension() == Level.OVERWORLD;
    }

    public boolean isInOtherDimensions() {
        return !this.isInOverworld() && !this.inEnd;
    }

    public boolean isInEnd() {
        return this.getMobData().isInEnd();
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public void setTarget(@Nullable LivingEntity target) {
        lostTargetTime = Maths.toTick(5);
        if (target != null) {
            this.setTargetId(target.getId());
            this.setTargetUuid(target.getUUID());
        }
    }

    protected float getDamageAfterMagicAbsorb(DamageSource p_21193_, float p_21194_) {
        if (this.isNihilistic()) {
            return p_21194_;
        }
        if (p_21193_.is(DamageTypeTags.WITCH_RESISTANT_TO) && this.hardOrNormal()) {
            p_21194_ *= 0.5f;
        }
        return super.getDamageAfterMagicAbsorb(p_21193_, p_21194_);
    }

    protected float getDamageAfterArmorAbsorb(DamageSource damage, float amount) {
        if (this.isNihilistic()) {
            return amount;
        }
        amount = Math.min(amount, NoixmodAPIAttributesConfig.apostleDamageCap.get().floatValue());
        if (damage.is(DamageTypeTags.NO_ANGER)) {
            amount *= 0.25f;
        }
        if (damage.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            amount *= 0.5f;
        }
        return super.getDamageAfterArmorAbsorb(damage, amount);
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        if (p_20122_.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_FIRE)) {
            return true;
        }
        if (p_20122_.is(DamageTypeTags.IS_LIGHTNING)) {
            return true;
        }
        return super.isInvulnerableTo(p_20122_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.getHurtCooldown() > 0) {
            return false;
        } else {
            float cap = NoixmodAPIAttributesConfig.apostleDamageCap.get().floatValue();
            if (pAmount >= 15f) {
                arrowDamage -= 0.25F;
            }
            pAmount = Math.min(cap, pAmount);
            Entity entity = pSource.getEntity();
            if (this.isEasy()) {
                pAmount *= 1.5f;
            } else {
                pAmount *= 0.85f;
            }
            if (this.inEnd) {
                pAmount *= 0.75F;
            }
            if (this.isInOtherDimensions()) {
                pAmount *= 0.85F;
            }
            if (this.isHalfHealth()) {
                pAmount *= 0.85f;
                if (pSource.is(DamageTypeTags.IS_PROJECTILE)) {
                    return false;
                }
                if (entity instanceof Projectile || entity instanceof IProjectile) {
                    return false;
                }
            }
            if (invTime > 0) {
                return false;
            }
            if (this.isSettingSecondPhase()) {
                return false;
            }
            if (pSource.is(DamageTypeTags.IS_FALL)) {
                return false;
            }
            if (pSource.is(DamageTypeTags.IS_LIGHTNING)) {
                return false;
            }
            if (entity instanceof LivingEntity living) {
                ItemStack stack = living.getMainHandItem();
                if (stack.getEnchantmentLevel(ApiEnchantments.NIHILISTIC_KILLER.get()) > 0) {
                    if (this.getCancelRegenTick() <= 0 && !NoixmodAPIMainConfig.HorrorMode.get()) {
                        boolean flag = this.isNihilistic() || getTitleNumber() == 3;
                        setCancelRegenTick(flag ? Maths.toTick(1) : Maths.toTick(3));
                    }
                }
            }
            if (this.isNihilistic()) {
                pAmount = 4.0F;
            }
            setHurtCooldown(20);
            arrowDamage -= pSource.getEntity() instanceof Player ? 1.0F : 0.05F;
            hurtCount++;
            if (!this.isCastingSpell() && !this.isSettingSecondPhase()) {
                if (hurtCount >= (isSecondPhase() ? 2 : 4)) {
                    teleport();
                    hurtCount = 0;
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource ds, float var0) {
        if (this.isSettingSecondPhase()) {
            return;
        }
        float f = this.isNihilistic() ? 4F : Math.min(var0, NoixmodAPIAttributesConfig.apostleDamageCap
                .get().floatValue());
        super.actuallyHurt(ds, f);
    }

    public final void kill() {
        this.hurt(this.damageSources().genericKill(), 12.5f);
    }

    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {
        int ran = this.getRandomUtil().nextInt(10);
        this.healSelf(pEntity.getMaxHealth() / 20.0F);
        if (this.getOwner() != null) {
            this.getOwner().heal(pEntity.getMaxHealth() / 40.0F);
        }
        if (this.isSecondPhase()) {
            WrongedSoul soul = new WrongedSoul(NoixmodAPIEntities.WRONGED_SOUL.get(), this.level());
            soul.moveTo(pEntity.getX(), pEntity.getY(), pEntity.getZ());
            soul.setOwner(this);
            WorldUtil.nullableFinalizeSpawn(soul, pLevel, pLevel.getCurrentDifficultyAt(
                    pEntity.blockPosition()), MobSpawnType.MOB_SUMMONED);
            pLevel.addFreshEntity(soul);
        }
        if (this.arrowDamage < 4.0F) {
            this.arrowDamage = 4.0F;
        }
        this.setTraits(ran);
        return super.killedEntity(pLevel, pEntity);
    }

    public int getAmbientSoundInterval() {
        return 200;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return NoixmodAPISounds.APOSTLE_HURT_HORROR.get();
        } else {
            if (GoetyCompat.goetyLoaded()) {
                return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocations.parse("goety:apostle_hurt"));
            }
            return NoixmodAPISounds.APOSTLE_HURT.get();
        }
    }

    protected SoundEvent getDeathSound() {
        return NoixmodAPISounds.APOSTLE_DEATH.get();
    }

    public NihilistArmPose getArmPose() {
        if (this.isAlive()) {
            if (this.isSettingSecondPhase() || this.isCastingSpell()) {
                return NihilistArmPose.SPELL_AND_WEAPON;
            } else if (this.isAggressive()) {
                return NihilistArmPose.BOW_AND_ARROW;
            } else if (!this.getLord().isEmpty()) {
                return NihilistArmPose.SPELL_CASTING;
            } else {
                return NihilistArmPose.CROSSED;
            }
        } else {
            return NihilistArmPose.DIE;
        }
    }

    public void die(DamageSource p_21014_) {
        if (!ForgeHooks.onLivingDeath(this, p_21014_)) {
            this.causeKilled = p_21014_;
            LivingEntity livingentity = this.getKillCredit();
            if (this.deathScore >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.deathScore, p_21014_);
            }
            this.getCombatTracker().recheckStatus();
            Level level = this.level();
            level.broadcastEntityEvent(this, (byte)3);
        }
    }

    public int getTitleNumber() {
        if (!(this instanceof ApostleBoss)) {
            return 0;
        } else {
            if (this.getHealth() > this.getMaxHealth() / 12 * 11) {
                return 11;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 10) {
                return 10;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 9) {
                return 9;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 8) {
                return 8;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 7) {
                return 7;
            } else if (this.getHealth() > this.getMaxHealth() / 2) {
                return 6;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 5) {
                return 5;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 4) {
                return 4;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 3) {
                return 3;
            } else if (this.getHealth() > this.getMaxHealth() / 12 * 2) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    public boolean isNihilistic() {
        return this.getTitleNumber() == 1;
    }

    public boolean isRisingRedPlum() {
        return this.getTitleNumber() == 10;
    }

    public void handleTitleEvents() {
        if (this.getTitleNumber() == 9) {
            if (this.lavaCooldown <= 0) {
                LavaTrapSpell spell = new LavaTrapSpell(2);
                spell.castSpell(this.serverLevel(), this);
                this.lavaCooldown = 30 + this.getRandomUtil().nextInt(4);
            }
        } else if (this.getTitleNumber() == 8) {
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(4),
                    entity -> !MobUtils.areAllies(this, entity) && MobUtils.canHurt(entity, this));
            if (entities.isEmpty()) {
                return;
            }
            for (LivingEntity living : entities) {
                living.addDeltaMovement(Vec9.moveToVec(this, living, 0.05));
            }
        } else if (this.getTitleNumber() == 5) {
            if (this.getTarget() == null || this.getRandomUtil().nextInt(3) != 0 || this.isAfraid()) {
                return;
            }
            int i = this.getTicksUsingItem();
            this.performRangedAttack(this.getTarget(), BowItem.getPowerForTime(i) / 1.5F);
        } else if (this.getTitleNumber() == 2) {
            if (this.getTarget() == null) {
                return;
            }
            this.getTarget().addEffect(new MobEffectInstance(MobEffects.DARKNESS, 10, 0));
        }
    }

    public Component getDisplayName() {
        if (this.bossEvent == null) {
            return super.getDisplayName();
        }
        return this.bossEvent.getName();
    }

    public boolean fireImmune() {
        return true;
    }

    @Nullable
    protected SoundEvent getCastingSoundEvent() {
        if (GoetyCompat.goetyLoaded()) {
            return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocations.parse("goety:apostle_cast_spell"));
        }
        return NoixmodAPISounds.APOSTLE_CAST_SPELL.get();
    }

    private SoundEvent getShootSound() {
        SoundEvent apostleShoot = null;
        if (GoetyCompat.goetyLoaded()) {
            apostleShoot = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocations.parse("goety:apostle_shoot"));
        }
        return apostleShoot == null ? SoundEvents.SKELETON_SHOOT : apostleShoot;
    }

    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    protected SpellCastType getSpellCastType() {
        return SpellCastType.NIHILISTIC;
    }

    public boolean isPowered() {
        return this.isHalfHealth() && this.isSecondPhase();
    }

    public void performRangedAttack(LivingEntity $$0, float $$1) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil
                .getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(itemstack, (float)($$1 * this.getArrowDamage()));
        if (this.getMainHandItem().getItem() instanceof BowItem bow) {
            arrow = bow.customArrow(arrow);
        }
        double $$4 = $$0.getX() - this.getX();
        double $$5 = $$0.getY(0.5) - this.getY(0.5);
        double $$6 = $$0.getZ() - this.getZ();
        float speed = this.isSecondPhase() ? 3.6f : 2.4f;
        arrow.shoot($$4, $$5, $$6, speed, this.isSecondPhase() ? 1.0f : 0.8f);
        this.playSound(this.getShootSound(), 1.0f, 1.0f / (this.getRandom().nextFloat()
                * 0.4f + 0.8f));
        this.level().addFreshEntity(arrow);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        NihilisticArrow arrow = new NihilisticArrow(this.level(), this);
        arrow.setEffectsFromItem(stack);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        int t = this.isSecondPhase() ? 1 : 0;
        arrow.setOwner(this);
        arrow.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, t));
        if (BlueOceansCompat.isLoaded() && this.isRisingRedPlum())
            arrow.addEffect(new MobEffectInstance(BlueOceansCompat.getEffect("plum_invade"),
                    40, 0));
        arrow.setCritArrow(this.isInEnd() || this.random.nextFloat() < 0.05F);
        arrow.setBaseDamage(this.getArrowDamage());
        return arrow;
    }

    public float getArrowDamage() {
        float var = 0.0F;
        if (!this.isInEnd()) {
            if (this.isHalfHealth()) {
                var += 0.25F;
            }
            if (this.isHard()) {
                var += 0.25F;
            }
            if (this.getTitleNumber() == 3 || this.getTitleNumber() == 1) {
                this.arrowDamage += 1.0F;
            }
        }
        float finalDamage = Math.max(this.arrowDamage + var, 4.0F) + this.getSpellPower() / 1000.0F;
        if (BlueOceansCompat.isLoaded() && this.isRisingRedPlum())
            return finalDamage * 1.1F;
        return finalDamage;
    }

    public void setArrowDamage(float damage) {
        this.arrowDamage = damage;
    }

    public void setArrowDamagePlus() {
        this.arrowDamage++;
    }

    public void healSelf(float amount) {
        MobUtils.healLiving(this, amount);
    }

    public void setCastingSpeed() {
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        if (this.isFast()) {
            if (!speed.hasModifier(FAST_SPEED)) {
                speed.addTransientModifier(FAST_SPEED);
            }
        } else {
            if (speed.hasModifier(FAST_SPEED)) {
                speed.removeModifier(FAST_SPEED);
            }
        }
        if (this.isCastingSpell() && !isInEnd()) {
            if (!speed.hasModifier(CASTING_SPEED))
                speed.addTransientModifier(CASTING_SPEED);
        } else {
            if (speed.hasModifier(CASTING_SPEED))
                speed.removeModifier(CASTING_SPEED);
        }
        if (this.statueSpeedTime > 0) {
            if (!speed.hasModifier(STATUE_COOLDOWN_SPEED)) {
                speed.addTransientModifier(STATUE_COOLDOWN_SPEED);
            }
        } else {
            if (speed.hasModifier(STATUE_COOLDOWN_SPEED)) {
                speed.removeModifier(STATUE_COOLDOWN_SPEED);
            }
        }
        if (this.isSettingSecondPhase()) {
            if (!speed.hasModifier(ZERO_SPEED)) {
                speed.addTransientModifier(ZERO_SPEED);
            }
        } else {
            if (!this.isDeadOrDying() && speed.hasModifier(ZERO_SPEED)) {
                speed.removeModifier(ZERO_SPEED);
            }
        }
        if (this.isDeadOrDying()) {
            if (!speed.hasModifier(ZERO_SPEED)) {
                speed.addTransientModifier(ZERO_SPEED);
            }
        } else {
            if (!this.isSettingSecondPhase() && speed.hasModifier(ZERO_SPEED)) {
                speed.addTransientModifier(ZERO_SPEED);
            }
        }
    }

    /*-1 = Soul & Stay & None
    3 = Fireball & Fireball rain
    4 = Statue
    5 = ArrowRain
    6= Shadow
    7=Clone
    8=ZombieServant & Nihilistic
    9 = Trial
    10=Armor
    */
    public void setApostleSpell() {
        Random pRandom = this.getRandomUtil();
        boolean flag = pRandom.nextBoolean();
        float chance = pRandom.nextFloat();
        boolean secondPhase = this.isSecondPhase();
        if (secondPhase) {
            if (this.getCooldown() <= 5) {
                if (flag) {
                    if (chance >= 0.9F) {
                        this.setApostleSpell(9);
                    } else if (chance >= 0.8F) {
                        this.setApostleSpell(5);
                    }
                } else {
                    if (chance <= 0.3F && this.getStatueCooldown() <= 0) {
                        this.setApostleSpell(4);
                    } else if (this.getSpreadFireballTicks() <= 0 && chance <= 0.2F) {
                        this.setApostleSpell(3);
                    }
                }
            } else {
                this.setApostleSpell(-1);
            }
        } else {
            if (this.getCooldown() <= 5) {
                if (chance > 0.95F) {
                    this.setApostleSpell(5);
                } else if (chance > 0.8F) {
                    this.setApostleSpell(8);
                } else {
                    this.setApostleSpell(3);
                }
            } else {
                this.setApostleSpell(-1);
            }
        }
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

    protected void tickDeath() {
        ++this.trueDeathTime;
        this.setNoGravity(true);
        boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        if (this.trueDeathTime == 1) {
            if (!this.level().isClientSide) {
                this.playSound(SoundEvents.GENERIC_EXPLODE);
            }
            this.resetSpreadFireball();
        }
        if (this.trueDeathTime < Maths.toTick(10)) {
            this.move(MoverType.SELF, new Vec3(0, 0.23, 0));
        }
        if (this.trueDeathTime == Maths.toTick(10)) {
            this.playSound(SoundEvents.GLASS_BREAK);
            if (!this.level().isClientSide)
                this.serverLevel().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                                Blocks.CRYING_OBSIDIAN.defaultBlockState()),
                        this.getX(), this.getY() + 2, this.getZ(),
                        30, 0.5, 0, 0.5, 0);
        }
        if (this.trueDeathTime > Maths.toTick(10)) {
            this.move(MoverType.SELF, new Vec3(0, -3, 0));
        }
        if (this.fallDistance <= 1 && this.trueDeathTime >= Maths.toTick(11) - 10) {
            if (!this.level().isClientSide) {
                this.serverLevel().sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(),
                        200, 0.5, 0, 0.5, 0.5);
                if (this.causeKilled != null && flag) {
                    this.dropAllDeathLoot(this.causeKilled);
                    ExperienceOrb.award(this.serverLevel(), Vec3.ZERO, this.getExperienceReward());
                }
            }
            if (NoixmodAPIMainConfig.HorrorMode.get()) {
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(
                        ResourceLocations.parse("goety:apostle_predeath")
                );
                if (GoetyCompat.goetyLoaded() && event != null)
                    this.playSound(event);
                else
                    this.playSound(NoixmodAPISounds.APOSTLE_DEATH.get());
            } else {
                this.playSound(NoixmodAPISounds.APOSTLE_DEATH.get());
            }
            this.removeBecauseKilled();
        }
    }

    protected void removeBecauseKilled() {
        this.remove(RemovalReason.KILLED);
    }

    public boolean isHoldingBow() {
        return this.isHolding(is -> is.getItem() instanceof BowItem);
    }

    protected void tickEffects() {
        this.getActiveEffects().removeIf(NoixmodAPITags.CAN_NOT_EFFECT_APOSTLE);
        super.tickEffects();
    }

    public void forceAddEffect(MobEffectInstance p_147216_, @Nullable Entity p_147217_) {
        if (NoixmodAPITags.CAN_EFFECT_APOSTLE.test(p_147216_.getEffect())) {
            super.forceAddEffect(p_147216_, p_147217_);
        }
    }

    protected void onEffectUpdated(MobEffectInstance p_147192_, boolean p_147193_, @Nullable Entity p_147194_) {
        if (NoixmodAPITags.CAN_EFFECT_APOSTLE.test(p_147192_.getEffect())) {
            super.onEffectUpdated(p_147192_, p_147193_, p_147194_);
        }
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return NoixmodAPITags.CAN_EFFECT_APOSTLE.test(p_147208_.getEffect())
                && super.addEffect(p_147208_, p_147209_);
    }

    protected void onEffectAdded(MobEffectInstance p_147190_, @Nullable Entity p_147191_) {
        if (NoixmodAPITags.CAN_EFFECT_APOSTLE.test(p_147190_.getEffect())) {
            super.onEffectAdded(p_147190_, p_147191_);
        }
    }

    public boolean checkRainWeather(Level level) {
        return level.getRainLevel(1f) <= 0.4f && !this.isRemoved();
    }

    public int healTick() {
        int i = 30;
        if (this.getTitleNumber() == 4 || this.getTitleNumber() == 1) {
            i = 20;
        }
        if (this.isInEnd()) {
            i /= 2;
        }
        return i;
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide && this.isNihilistic()) {
            this.clientLevel().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(),
                    this.getRandomX(0.8), this.getRandomY(), this.getRandomZ(0.8),
                    0, 0, 0);
            this.clientLevel().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(),
                    this.getRandomX(0.8) + Math.random(), this.getRandomY(),
                    this.getRandomZ(0.8) + Math.random(), 0, 0, 0);
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
                            Math.max(0, this.random.nextInt(6) - 4), -6.0F + this.random.nextInt(13) * 0.5);
                }
                for (l = 0; l < 16; ++l) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5),
                            this.getRandomY(), this.getZ(0.5), 0.0, 0.0, 0.0);
                }
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE,
                        this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
        if (!this.level().isClientSide) {
            LivingEntity lie = this.getTarget();
            List<Mob> target = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(64), living
                    -> MobUtils.isAlive(living) && MobUtils.canHurt(living, this) && (living.getTarget() == this ||
                    living.getTarget() instanceof Ownable ownable && ownable.getOwner() == this));
            if (lie == null && !target.isEmpty())
            {
                target.stream().findAny().ifPresent(living -> {
                    this.setTarget(living);
                    this.setApostleTarget(living);
                });
            }
            if (GoetyCompat.goetyLoaded() && this.isBoss())
            {
                var entity = ValueHolder.nullToOther(this.getTarget(), this.getApostleTarget());
                if (entity != null && entity.getType() == ForgeRegistries.ENTITY_TYPES.getValue(
                        ResourceLocations.parse("goety:apostle")))
                {
                    entity.setPosRaw(Double.NaN, Double.NaN, Double.NaN);
                    entity.discard();
                }
            }
            if (!this.getApostleTargets().isEmpty())
            {
                this.setApostleTarget(this.getApostleTargets().stream().findAny().orElse(null));
            }
            if (this.isSecondPhase()) {
                int i = this.isHalfHealth() ? 2 : 1;
                if (this.isHard() || this.isInEnd()) {
                    if (this.tickCount % (this.healTick() / i) == 0) {
                        if (this.getCancelRegenTick() <= 0 && !this.isNihilistic()) {
                            this.healSelf(this.getMaxHealth() / 320F);
                        }
                        if (this.isOwned()) {
                            this.getOwner().heal(0.25F);
                        }
                    }
                }
                if (this.checkRainWeather(serverLevel()))
                    serverLevel().setWeatherParameters(0, ServerLevel.RAIN_DURATION.getMaxValue(),
                            true, true);
            }
            if (!(this.getMainHandItem().is(Items.BOW))) {
                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
            }
        } else {
            if (this.isDeadOrDying()) {
                float f8 = (this.random.nextFloat() - 0.5F) * 8.0F;
                float f9 = (this.random.nextFloat() - 0.5F) * 4.0F;
                float f11 = (this.random.nextFloat() - 0.5F) * 8.0F;
                this.level().addParticle(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(), this.getX() + f8,
                        this.getY() + 2.0 + f9, this.getZ() + f11, 0.0,
                        0.0, 0.0);
            }
        }
    }

    static {
        DATA_TARGET_ID = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DATA_TARGET_UUID = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_HURT_COOLDOWN = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DATA_CANCEL_REGEN_TIME = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        DATA_PHASE = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BYTE);
        DATA_SETTING_SECOND = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.BOOLEAN);
        DATA_APOSTLE_SPELL = SynchedEntityData.defineId(Apostle.class, EntityDataSerializers.INT);
        CASTING_SPEED = new AttributeModifier("1Player_IX2-931-Apostle-CastingSpeed",
                -0.5, AttributeModifier.Operation.ADDITION);
        FAST_SPEED = new AttributeModifier("1Player_IX2-931-Apostle-FastSpeed",
                0.25, AttributeModifier.Operation.ADDITION);
        STATUE_COOLDOWN_SPEED = new AttributeModifier("1Player_IX2-931-Apostle-StatueCooldown",
                -0.2, AttributeModifier.Operation.ADDITION);
        ZERO_SPEED = new AttributeModifier("1Player_IX2-931-Apostle-ZeroSpeed",
                -10, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    {
        this.dangerDouble = 6;
        this.arrowDamage = 4.0F;
    }

    protected static class ApostleBowAttackGoal extends Goal {
        private final Apostle mob;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public ApostleBowAttackGoal(Apostle pApostle) {
            this.mob = pApostle;
            this.attackRadiusSqr = 30F * 30F;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getTarget() != null && this.mob.getTarget().isAlive() && this.isHoldingBow();
        }

        protected boolean isHoldingBow() {
            return this.mob.isHoldingBow();
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.navigation.isDone()) && this.isHoldingBow()
                    && !this.mob.isCastingSpell() && !this.mob.isSettingSecondPhase();
        }

        public void start() {
            this.mob.setAggressive(true);
        }

        public void stop() {
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.mob.stopUsingItem();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }
                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }
                if (d0 <= this.attackRadiusSqr) {
                    this.mob.navigation.stop();
                    ++this.strafingTime;
                } else {
                    this.mob.navigation.moveTo(livingentity, 1.0D);
                    this.strafingTime = -1;
                }
                if (this.strafingTime >= 20) {
                    if (this.mob.level().getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }
                    if (this.mob.level().getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }
                    this.strafingTime = 0;
                }
                if (this.strafingTime > -1) {
                    if (d0 > this.attackRadiusSqr * 0.75F) {
                        this.strafingBackwards = false;
                    } else if (d0 < this.attackRadiusSqr * 0.25F) {
                        this.strafingBackwards = true;
                    }
                    this.mob.moveControl.strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.lookControl.setLookAt(livingentity, 30.0F, 30.0F);
                }
                if (!this.mob.isSettingSecondPhase()) {
                    if (this.mob.isUsingItem()) {
                        if (!flag && this.seeTime < -120) {
                            this.mob.setAggressive(false);
                            this.mob.setTarget(null);
                            this.mob.setApostleTarget(null);
                            this.mob.stopUsingItem();
                        } else if (flag) {
                            int i = this.mob.getTicksUsingItem();
                            final int j = 20;
                            if (i >= j) {
                                this.mob.stopUsingItem();
                                this.mob.performRangedAttack(livingentity, (BowItem.getPowerForTime(i) / 1.5f));
                            }
                            int cooldown = NoixmodAPIAttributesConfig.apostleArcheryCooldown.get();
                            this.attackTime = this.mob.isAfraid() ? cooldown * 2 : cooldown;
                        }
                    } else if (--this.attackTime <= 0 && this.seeTime >= -120) {
                        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
                    }
                }
            }
        }
    }

    protected static class ApostleLookAtEntityGoal
            extends HorrorLookAtEntityGoal {
        protected final Apostle apostle;

        public ApostleLookAtEntityGoal(Apostle p_25520_) {
            super(p_25520_);
            this.apostle = p_25520_;
        }

        public boolean canUse() {
            if (this.apostle.isSettingSecondPhase()) {
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.apostle.isSettingSecondPhase()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    protected static class ApostleRandomStrollGoal
            extends RandomStrollGoal {
        protected final Apostle apostle;

        public ApostleRandomStrollGoal(Apostle p_25734_, double p_25735_) {
            super(p_25734_, p_25735_);
            this.apostle = p_25734_;
        }

        public boolean canUse() {
            if (this.apostle.isSettingSecondPhase()) {
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.apostle.isSettingSecondPhase()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    protected static class ApostleRandomLookGoal
    extends Goal {
        protected final Apostle mob;
        protected double relX;
        protected double relZ;
        protected int lookTime;
        public ApostleRandomLookGoal(Apostle apostle) {
            this.mob = apostle;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            if (NoixmodAPIMainConfig.HorrorMode.get()) {
                return false;
            }
            return this.mob.getRandom().nextFloat() < 0.02F && !this.mob.isSettingSecondPhase();
        }

        public boolean canContinueToUse() {
            return this.lookTime >= 0 && !NoixmodAPIMainConfig.HorrorMode.get();
        }

        public void start() {
            double $$0 = 6.283185307179586 * this.mob.getRandom().nextDouble();
            this.relX = Math.cos($$0);
            this.relZ = Math.sin($$0);
            this.lookTime = 20 + this.mob.getRandom().nextInt(20);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.lookTime;
            this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX,
                    this.mob.getEyeY(), this.mob.getZ() + this.relZ);
        }
    }

    protected abstract class SpellGoal
    extends UseSpellGoal {
        protected final Apostle apostle;

        public SpellGoal(Apostle apostle) {
            this.apostle = apostle;
        }

        public void start() {
            super.start();
        }

        protected float difficult() {
            if (this.apostle.isEasy()) {
                return 0;
            } else if (this.apostle.isNormal()) {
                return 25;
            } else {
                return 50;
            }
        }

        protected int cooldownImm() {
            if (this.apostle.reallyDangerous()) {
                return 10;
            }
            return 0;
        }

        public void tick() {
            super.tick();
            if (this.attackWarmupDelay == 0) {
                this.apostle.setCooldown(this.getCoolDown() - this.cooldownImm());
                if (this.needPower()) {
                    this.apostle.setSpellPower(this.apostle.getSpellPower() - (this.getSpellPower() - this.difficult()));
                    this.apostle.setArrowDamagePlus();
                } else {
                    this.apostle.hurtCount++;
                }
                this.stop();
            }
        }

        public void stop() {
            super.stop();
            this.apostle.stopSpell();
        }

        protected boolean isSecondPhase() {
            return this.apostle.isSecondPhase();
        }

        protected int getCoolDown() {
            return 40;
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.apostle.isSettingSecondPhase();
        }

        public boolean canUse() {
            if (this.needPower()) {
                if (this.apostle.getSpellPower() < this.getSpellPower()) {
                    return false;
                }
                if (this.apostle.getRandom().nextFloat() > 0.2F) {
                    return false;
                }
            }
            return this.notNihilistic() && super.canUse() && this.checkBoss();
        }

        protected boolean notNihilistic() {
            return !this.apostle.isNihilistic();
        }

        protected boolean isInEnd() {
            return this.apostle.isInEnd();
        }

        protected abstract boolean needPower();

        protected float getSpellPower() {
            return 400;
        }

        protected boolean checkTarget() {
            LivingEntity entity = this.apostle.getApostleTarget();
            return super.checkTarget() || (entity != null && entity.isAlive());
        }

        protected boolean checkBoss() {
            if (this.apostle instanceof ApostleBoss) {
                if (this.needPower()) {
                    return this.apostle.getSpell() == this.getSpells();
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }

        protected int getSpells() {
            return -1;
        }

        @Nullable
        protected SoundEvent prepareSound() {
            return ForgeRegistries.SOUND_EVENTS
                    .getValue(ResourceLocations.parse("goety:apostle_prepare_spell"));
        }

        @Nullable
        protected SoundEvent summonSound() {
            return ForgeRegistries.SOUND_EVENTS
                    .getValue(ResourceLocations.parse("goety:apostle_prepare_summon"));
        }
    }

    protected static class CloneSettingSecondPhaseGoal
            extends Goal {
        protected final ApostleServant clone;

        public CloneSettingSecondPhaseGoal(ApostleServant mob) {
            this.clone = mob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public void start() {
            super.start();
            this.clone.getNavigation().stop();
            this.clone.setSettingSecondPhase(true);
        }

        public boolean canUse() {
            if (this.clone.tickCount >= 6000 && this.clone.getOwner() instanceof Apostle) {
                if (this.clone.isSecondPhase()) {
                    return false;
                }
                return this.clone.getHealth() < this.clone.getMaxHealth() && !this.clone.isSettingSecondPhase();
            } else {
                return !this.clone.isSettingSecondPhase() && this.clone.isHalfHealth() && !this.clone.isSecondPhase();
            }
        }

        public boolean canContinueToUse() {
            if (this.clone.isSecondPhase()) {
                return false;
            }
            if (this.clone.isSettingSecondPhase()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    protected static class SetSecondPhaseGoal
            extends Goal {
        protected final Apostle apostle;

        public SetSecondPhaseGoal(Apostle ap) {
            this.apostle = ap;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public void start() {
            super.start();
            this.apostle.getNavigation().stop();
            this.apostle.setSettingSecondPhase(true);
        }

        public boolean canUse() {
            return !this.apostle.isSecondPhase() && this.apostle.isHalfHealth();
        }
    }

    protected class SummonSoulGoal
            extends SpellGoal {
        public SummonSoulGoal(Apostle apostle) {
            super(apostle);
        }

        protected void castSpell() {
            this.apostle.summonSoul();
        }

        public boolean canUse() {
            if (!this.apostle.isServant()) {
                if (!this.isSecondPhase()) {
                    return false;
                }
            }
            return super.canUse();
        }

        protected int getCastWarmupTime() {
            return 21;
        }

        protected boolean needPower() {
            return false;
        }

        protected int getCastingTime() {
            return 40;
        }

        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 20) {
                this.castSpell();
            }
            if (this.attackWarmupDelay == 15 && this.isInEnd()) {
                this.castSpell();
            }
            if (this.attackWarmupDelay == 10) {
                this.castSpell();
            }
            if (this.attackWarmupDelay == 5 && this.isInEnd()) {
                this.castSpell();
            }
            if (this.attackWarmupDelay == 0) {
                this.nextAttackTickCount = this.apostle.tickCount + this.getCastingInterval();
                this.castSpell();
                this.apostle.setCooldown(this.getCoolDown());
                this.apostle.stopSpell();
            }
        }

        protected int getCastingInterval() {
            if (this.apostle.getTitleNumber() == 7) {
                return 300;
            }
            return 350;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.DARK;
        }

        protected float getSpellPower() {
            return 40;
        }
    }

    protected class RoarSpellGoal
            extends SpellGoal {
        public RoarSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected void castSpell()
        {
            if (this.apostle.isSecondPhase()) {
                this.apostle.roar();
            } else {
                this.apostle.quake();
            }
            this.apostle.serverLevel().sendParticles(new CircleParticleOption(0, 0, 0, 6, 0.3F),
                    this.apostle.getX(), this.apostle.getY() + 0.1, this.apostle.getZ(),
                    1, 0, 0, 0, 0);
            this.apostle.resetCooldown();
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 300;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.prepareSound();
            }
            return NoixmodAPISounds.APOSTLE_PREPARE_SPELL.get();
        }

        public boolean canUse() {
            if (this.apostle.getTarget() != null) {
                if (!(this.apostle.distanceToSqr(this.apostle.getTarget()) < Maths.square(6))) {
                    return false;
                }
            }
            return super.canUse();
        }

        protected boolean needPower() {
            return false;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.ATTACK;
        }

        protected int getSpells() {
            if (this.apostle.isSecondPhase()) {
                return -1;
            } else {
                return -2;
            }
        }
    }

    protected class SummonServantsSpellGoal
            extends SpellGoal {
        public SummonServantsSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected void castSpell() {
            for (int t = 0;t < this.apostle.getRandomUtil().nextInt(3) + 2;++t) {
                this.apostle.summonServants();
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            if (this.isInEnd()) {
                return 700;
            }
            return 800;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        public boolean canUse()
        {
            ServerLevel level = this.apostle.serverLevel();
            if (this.apostle.isSecondPhase()) {
                if (!OwnerSummon.canSummon(level, OwnableMob.ownerOrThis(this.apostle, this.apostle), 12,
                        lie -> lie instanceof NihilisticServant)) {
                    return false;
                }
            } else {
                if (!OwnerSummon.canSummon(level, OwnableMob.ownerOrThis(this.apostle, this.apostle), 12,
                        lie -> lie instanceof Golem)) {
                    return false;
                }
            }
            return super.canUse();
        }

        protected boolean needPower() {
            return true;
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.ZOMBIE;
        }

        protected float getSpellPower() {
            return 500;
        }

        protected int getSpells() {
            return 8;
        }
    }

    protected class ShootFireballGoal
            extends SpellGoal {
        public ShootFireballGoal(Apostle apostle) {
            super(apostle);
        }

        public boolean canUse() {
            if (this.apostle.isSecondPhase()) {
                return false;
            }
            if (!this.apostle.getRandomUtil().nextBoolean()) {
                return false;
            }
            return super.canUse();
        }

        protected boolean needPower() {
            return false;
        }

        protected boolean notNihilistic() {
            return true;
        }

        protected void castSpell() {
            this.apostle.summonFireball();
            if (this.apostle.isInDanger()) {
                this.apostle.teleport();
            }
            this.apostle.resetCooldown();
            this.apostle.setApostleSpell(-2);
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return Maths.toTick(3);
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.prepareSound();
            }
            return NoixmodAPISounds.APOSTLE_PREPARE_SPELL.get();
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.NIHILISTIC;
        }

        protected int getSpells() {
            return 3;
        }
    }

    protected class ArmoredZombieSpellGoal
            extends SpellGoal {
        public ArmoredZombieSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected void castSpell() {
            this.apostle.summonServants();
        }

        public boolean canUse() {
            if (this.apostle.isServerSide()) {
                if (!OwnerSummon.canSummon(this.apostle.serverLevel(), OwnableMob.ownerOrThis(this.apostle, this.apostle), 3,
                        zombie -> zombie instanceof Golem vindicator &&
                                (vindicator.getOwner() == OwnableMob.ownerOrThis(this.apostle, this.apostle)))) {
                    return false;
                }
            }
            return false;
        }

        protected boolean needPower() {
            return true;
        }

        protected int getCastingTime() {
            return 30;
        }

        protected int getCastingInterval() {
            return 700;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.ZOMBIE;
        }

        protected int getSpells() {
            return 8;
        }
    }

    protected class RangedSummonSpellGoal
            extends SpellGoal {
        public RangedSummonSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected void castSpell() {
            this.apostle.summonRangedServant();
        }

        public boolean canUse() {
            if (this.apostle.isServerSide()) {
                if (!OwnerSummon.canSummon(this.apostle.serverLevel(), OwnableMob.ownerOrThis(this.apostle, this.apostle), 6,
                        Apostle.ownerPredicate(this.apostle))) {
                    return false;
                }
            }
            return super.canUse();
        }

        protected boolean needPower() {
            return true;
        }

        protected int getCoolDown() {
            return 30;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            if (this.apostle.inEnd) {
                return 400;
            }
            return 500;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.RANGE;
        }

        protected int getSpells() {
            return 8;
        }
    }

    protected class TrialSpellGoal
            extends SpellGoal {
        double x;
        double y;
        double z;

        public TrialSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        public boolean canUse() {
            LivingEntity living = this.apostle.getTarget();
            if (!this.apostle.isSecondPhase()) {
                return false;
            }
            if (living == null) {
                return false;
            }
            if (!this.checkRange(living)) {
                return false;
            }
            return super.canUse();
        }

        protected boolean notNihilistic() {
            return true;
        }

        protected boolean checkRange(LivingEntity living) {
            return this.apostle.distanceToSqr(living) < Maths.square(4);
        }

        public void tick() {
            super.tick();
            if (this.apostle.getTarget() != null) {
                if (this.attackWarmupDelay == 30) {
                    this.x = this.apostle.getTarget().getX();
                    this.y = this.apostle.getTarget().getY();
                    this.z = this.apostle.getTarget().getZ();
                }
            }
        }

        protected void castSpell() {
            float f = NoixmodAPIMainConfig.HorrorMode.get() ? 99 : 0;
            this.apostle.cancelHealTick += 99;
            if (this.apostle.getTarget() != null) {
                MobUtils.disableShield(8, 8, 8, this.apostle);
                MobUtils.rangeHurt(8, 8, 8, this.apostle, NoixmodAPIDamageSource.nihility(this.apostle),
                        (this.apostle.getTarget().getMaxHealth() / 4) + 32 + f);
                if (this.apostle.getTitleNumber() == 1 || this.apostle.getTitleNumber() == 3) {
                    this.apostle.healSelf(10F);
                }
                WorldUtil.getServerLevel(this.apostle).sendParticles(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(),
                        this.apostle.getX(), this.apostle.getY(), this.apostle.getZ(), 99, 5,
                        0, 5, 0);
            }
            List<LivingEntity> list = this.apostle.getApostleTargets();
            if (list.isEmpty()) {
                return;
            }
            for (LivingEntity living : list) {
                living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get()), living);
                living.hurt(this.apostle.damageSources().starve(), 10 + living.getMaxHealth() / 5);
                break;
            }
        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.prepareSound();
            }
            return NoixmodAPISounds.APOSTLE_PREPARE_SPELL.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.NIHILISTIC;
        }

        protected int getSpells() {
            return 9;
        }
    }

    protected class SummonStaySoulGoal
            extends SpellGoal {
        public SummonStaySoulGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return false;
        }

        protected void castSpell() {
            for (int i = 0;i < 5;++i) {
                this.apostle.summonStaySoul();
            }
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        public boolean canUse() {
            if (!this.apostle.isSecondPhase()) {
                return false;
            }
            return super.canUse();
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.DARK;
        }
    }

    protected class SpreadFireballGoal
            extends SpellGoal {
        public SpreadFireballGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        protected float getSpellPower() {
            return 45.5f;
        }

        public boolean canUse() {
            if (!this.apostle.isSecondPhase()) {
                return false;
            }
            return super.canUse();
        }

        protected void castSpell() {
            this.apostle.summonWither();
            this.apostle.setSpreadingFireball();
            this.apostle.resetCooldown();
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 9000;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.prepareSound();
            }
            return NoixmodAPISounds.APOSTLE_PREPARE_SPELL.get();
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.NIHILISTIC;
        }

        protected int getSpells() {
            return 3;
        }
    }

    protected class CloneSpellGoal
            extends SpellGoal {
        public CloneSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        protected void castSpell()
        {
            ServerLevel level = this.apostle.serverLevel();
            SummonApostle summonApostle = new SummonApostle(NoixmodAPIEntities.SUMMON_APOSTLE.get(), level);
            summonApostle.setBoss(false);
            summonApostle.setOwner(this.apostle);
            level.addFreshEntity(summonApostle);
            this.apostle.resetCooldown();
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 12000;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.NIHILISTIC;
        }

        public boolean canUse() {
            if (this.apostle.isServant() || this.apostle.isClone()) {
                return false;
            }
            if (!NoixmodAPIMainConfig.HorrorMode.get()) {
                return false;
            }
            return super.canUse();
        }

        protected boolean notNihilistic() {
            return true;
        }
    }

    protected class SummonStatueSpellGoal
            extends SpellGoal {
        public SummonStatueSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        protected void castSpell()
        {
            for (int i = 0;i < this.apostle.getRandomUtil().nextInt(2) + 1;++i) {
                ServerLevel level = this.apostle.serverLevel();
                NihilisticStatue statue = new NihilisticStatue(NoixmodAPIEntities.NIHILISTIC_STATUE.get(), level);
                BlockPos pos = this.apostle.blockPosition().offset(Maths.randomInteger(12), 0,
                        Maths.randomInteger(12));
                statue.setOwner(OwnableMob.ownerOrThis(this.apostle, this.apostle));
                statue.moveTo(pos, 0, 0);
                MobUtils.moveToGround(statue);
                statue.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED);
                level.addFreshEntity(statue);
                this.apostle.cancelHealTick += 40;
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return Maths.toTick(10);
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.NIHILISTIC;
        }

        public boolean canUse()
        {
            if (!OwnerSummon.canSummon(this.apostle.serverLevel(), OwnableMob.ownerOrThis(this.apostle, this.apostle), 4,
                    lie -> lie instanceof NihilisticStatue)) {
                return false;
            }
            if (this.apostle.getStatueCooldown() > 0) {
                return false;
            }
            return super.canUse();
        }

        protected int getSpells() {
            return 6;
        }
    }

    protected class SummonPowerEntitySpellGoal
            extends SpellGoal {
        public SummonPowerEntitySpellGoal(Apostle follower) {
            super(follower);
        }

        protected boolean needPower() {
            return true;
        }

        protected void castSpell() {
            for (int i = 0;i < 2;++i) {
                this.apostle.summonPower();
            }
            this.apostle.resetCooldown();
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 900;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.WATER;
        }

        protected float getSpellPower() {
            return 167.5f;
        }
    }

    protected class ShadowSpellGoal
            extends SpellGoal {
        public ShadowSpellGoal(Apostle apostle) {
            super(apostle);
        }

        public boolean canUse() {
            if (!this.apostle.isSecondPhase()) {
                return false;
            }
            if (this.apostle.isShadow()) {
                return false;
            }
            return super.canUse();
        }

        protected boolean needPower() {
            return true;
        }

        protected float getSpellPower() {
            return 100f;
        }

        protected void castSpell() {
            var n = NoixmodAPIMainConfig.HorrorMode.get() ? 2 : 0;
            for (int i = 0;i < 2 + n;++i) {
                this.apostle.summonShadow();
            }
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            if (this.apostle.getTitleNumber() == 1) {
                return Maths.toTick(60);
            }
            return Maths.toTick(80);
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.NIHILISTIC;
        }
    }

    protected class SummonArrowRainSpellGoal
            extends SpellGoal {
        public SummonArrowRainSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        protected void castSpell() {
            this.apostle.cancelHealTick += 20;
            this.apostle.summonEntity();
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            if (!this.apostle.isInEnd()) {
                return Maths.toTick(49);
            }
            return Maths.toTick(4);
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            if (GoetyCompat.goetyLoaded()) {
                return this.summonSound();
            }
            return NoixmodAPISounds.APOSTLE_SUMMON.get();
        }

        protected boolean notNihilistic() {
            return true;
        }

        protected APISpells.APISpell getSpell() {
            if (this.apostle.isHorror()) {
                return APISpells.APISpell.BLOOD;
            }
            return APISpells.APISpell.NIHILISTIC;
        }

        protected float getSpellPower() {
            return 199F;
        }

        public boolean canUse()
        {
            if (!OwnerSummon.canSummon(this.apostle.serverLevel(), this.apostle, 3, entity -> entity instanceof
                    NihilisticArrowRain)) {
                return false;
            }
            if (!NoixmodAPIMainConfig.HorrorMode.get() && this.apostle.getTitleNumber() != 1) {
                if (!this.apostle.inEnd && this.apostle.getRandomUtil().nextFloat() > 0.005f) {
                    return false;
                }
            }
            if (this.apostle.arrowRainCooldown > 0) {
                return false;
            }
            return super.canUse();
        }

        protected int getSpells() {
            return 5;
        }
    }

    protected class SummonArrowsSpellGoal extends SpellGoal {
        private int posId;
        private Vec3 pos;
        public SummonArrowsSpellGoal(Apostle apostle) {
            super(apostle);
        }

        protected boolean needPower() {
            return true;
        }

        protected float getSpellPower() {
            return 600.0F;
        }

        protected void castSpell() {
        }

        public void tick() {
            if (this.apostle.tickCount % 8 == 0) {
                if (!apostle.level().isClientSide) {
                    this.pos = this.getPos();
                    NihilisticArrow arrow = new NihilisticArrow(apostle.level(), apostle);
                    arrow.moveTo(apostle.position().add(this.pos));
                    arrow.setFloating(apostle.getTarget());
                    apostle.level().addFreshEntity(arrow);
                    ++posId;
                }
            }
            super.tick();
        }

        private Vec3 getPos() {
            if (posId == 0)
                return new Vec3(0, 2, 0);
            else if (posId == 1)
                return new Vec3(0.3, 1.75, 0);
            else if (posId == 2)
                return new Vec3(0.5, 1.6, 0);
            else if (posId == 3)
                return new Vec3(0.8, 1.5, 0);
            else if (posId == 4)
                return new Vec3(0, 1.6, 0.5);
            else if (posId == 5)
                return new Vec3(0, 1.5, 0.8);
            else if (posId == 6)
                return new Vec3(0.5, 2.2, 0);
            else if (posId == 7)
                return new Vec3(0, 2.2, 0.5);
            else if (posId == 8)
                return new Vec3(0, 2.2, 0);
            else
                return new Vec3(0, 1.4, 0);
        }

        public void stop() {
            super.stop();
            this.posId = 0;
        }

        public boolean canUse() {
            return apostle.isSecondPhase() && super.canUse();
        }

        protected int getCastingTime() {
            return 50;
        }

        protected int getCastingInterval() {
            return 600;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

        protected float getCastVolume() {
            return 5.0F;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.UNKNOWN;
        }
    }

    protected static class ApostleAttackPlayerGoal extends NearestAttackableTargetGoal<Player> {
        public ApostleAttackPlayerGoal(Apostle p_199891_) {
            super(p_199891_, Player.class, true, player -> player instanceof Player players
                    && !(players.getDisplayName().getString().equals("Player_9") &&
                    players.getInventory().contains(NoixmodAPIItems.HALO_OF_APOSTLE.get().getDefaultInstance())));
        }
    }
}