
package com.github.NineAbyss9.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.ObjectUtil;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ApiPathfinderMob
extends PathfinderMob {
    protected static final EntityDataAccessor<Boolean> DATA_BABY;
    protected final MobUtils mobUtils = new MobUtils(this);
    protected final WorldUtil worldUtil = new WorldUtil(this);
    protected final MobData mobData = new MobData(this);
    protected static final boolean FALSE = ObjectUtil.FALSE;
    protected static final boolean TRUE = ObjectUtil.TRUE;
    public Boolean enabledHorrorMode = NoixmodAPIMainConfig.HorrorMode.get();
    private final OwnerSummon ownerSummon = new OwnerSummon(this);
    protected Random randomUtil = new Random();
    protected ApiPathfinderMob(EntityType<? extends ApiPathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY, false);
    }

    @SuppressWarnings("deprecation")
    public boolean canBreatheUnderwater() {
        return super.canBreatheUnderwater();
    }

    public void aiStep() {
        this.updateSwingTime();
        if (this instanceof Enemy) {
            this.updateNoActionTime();
        }
        super.aiStep();
    }

    public SoundSource getSoundSource() {
        return this instanceof Enemy ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        if (Float.isNaN(pAmount)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 60) {
            this.spawnAnim();
        } else super.handleEntityEvent(pId);
    }

    public void spawnAtLocation(Item item, int count) {
        for (int i = 0;i < count;i++) {
            ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                    ItemStacks.of(item));
            this.level().addFreshEntity(itementity);
        }
    }

    public boolean isServerSide() {
        return !this.level().isClientSide;
    }

    public MobData getMobData() {
        return this.mobData;
    }

    public void spawnAtLocation(Supplier<Item> supplier, int count) {
        for (int i = 0;i < count;i++) {
            ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                    ItemStacks.of(supplier.get()));
            this.level().addFreshEntity(itementity);
        }
    }

    public OwnerSummon getSummon() {
        return this.ownerSummon;
    }

    public void sendSystemMessage(Component p_216998_) {
        if (this.level().isClientSide) {
            Minecraft.getInstance().gui.getChat().addMessage(p_216998_);
        } else {
            serverLevel().getServer().getPlayerList().broadcastSystemMessage(p_216998_, false);
        }
    }

    public void setPos(double p_20210_, double p_20211_, double p_20212_) {
        if (Double.isNaN(p_20210_) || Double.isNaN(p_20211_) || Double.isNaN(p_20212_)) {
            return;
        }
        super.setPos(p_20210_, p_20211_, p_20212_);
    }

    public ServerLevel serverLevel() {
        return (ServerLevel)this.level();
    }

    public ClientLevel clientLevel() {
        return (ClientLevel)this.level();
    }

    public void setBaby(boolean p_21451_) {
        this.entityData.set(DATA_BABY, p_21451_);
    }

    public MobUtils getMobUtils() {
        return this.mobUtils;
    }

    public WorldUtil getWorldUtil() {
        return this.worldUtil;
    }

    protected void updateNoActionTime() {
        float f = this.getLightLevelDependentMagicValue();
        if (f > 0.5F) {
            this.noActionTime += 2;
        }
    }

    @Nullable
    public SoundEvent getStepSound() {
        return null;
    }

    public static AttributeSupplier.Builder createPathAttributes() {
        return ApiPathfinderMob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

    public Random getRandomUtil() {
        return this.randomUtil;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_BABY.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
                                        MobSpawnType pReason, @Nullable SpawnGroupData p_21437_,
                                        @Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_,
                                        MobSpawnType p_21436_) {
        return this.finalizeSpawn(p_21434_, p_21435_, p_21436_, null, null);
    }

    public boolean isImmobile() {
        return super.isImmobile();
    }

    @SuppressWarnings("deprecation")
    public static boolean hasChunkAt( Entity entity, BlockPos pos) {
        return entity.level().hasChunkAt(pos);
    }

    /*
    @SuppressWarnings("deprecation")
    public boolean hasChunkAt(BlockPos pos) {
        return this.level().hasChunkAt(pos);
    }
    */

    @SuppressWarnings("deprecation")
    public boolean hasChunkAt(int i, int j) {
        return this.level().hasChunkAt(i, j);
    }

    @SuppressWarnings("deprecation")
    public float getLightLevelDependentMagicValue() {
        return super.getLightLevelDependentMagicValue();
    }

    public Boolean isHorror() {
        return NoixmodAPIMainConfig.HorrorMode.get();
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    public boolean shouldDropExperience() {
        return true;
    }

    protected boolean shouldDropLoot() {
        return true;
    }

    protected void populateDefaultEquipment() {
        RandomSource source = this.level().random;
        DifficultyInstance instance = this.level().getCurrentDifficultyAt(this.blockPosition());
        this.populateDefaultEquipmentSlots(source, instance);
        this.populateDefaultEquipmentEnchantments(source, instance);
    }

    public ItemStack getProjectile(ItemStack pWeaponStack) {
        if (pWeaponStack.getItem() instanceof ProjectileWeaponItem item) {
            Predicate<ItemStack> predicate = item.getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return ForgeHooks.getProjectile(this, pWeaponStack, itemstack.isEmpty() ? new ItemStack(Items.ARROW)
                    : itemstack);
        } else {
            return ForgeHooks.getProjectile(this, pWeaponStack, ItemStack.EMPTY);
        }
    }

    static {
        DATA_BABY = SynchedEntityData.defineId(ApiPathfinderMob.class, EntityDataSerializers.BOOLEAN);
    }
}
