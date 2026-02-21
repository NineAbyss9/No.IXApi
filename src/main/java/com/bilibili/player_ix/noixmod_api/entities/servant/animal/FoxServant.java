
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.github.NineAbyss9.ix_api.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IAgeableMob;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.IntFunction;

public class FoxServant extends AgeableAnimalServant {
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0;
    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1;
    Fox fox;
    public FoxServant(EntityType<? extends FoxServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 3;
        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);
        this.setCanPickUpLoot(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public int getExperienceReward() {
        return this.isHostile() ? super.getExperienceReward() : 0;
    }

    @Nullable
    @Override
    public IAgeableMob getBreedMob() {
        return super.getBreedMob();
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_218171_, DifficultyInstance p_218172_) {
        if (p_218171_.nextFloat() < 0.2F) {
            float f = p_218171_.nextFloat();
            ItemStack itemstack;
            if (f < 0.05F) {
                itemstack = new ItemStack(Items.EMERALD);
            } else if (f < 0.2F) {
                itemstack = new ItemStack(Items.EGG);
            } else if (f < 0.4F) {
                itemstack = p_218171_.nextBoolean() ? ItemStacks.of(Items.RABBIT_FOOT) :
                        ItemStacks.of(Items.RABBIT_HIDE);
            } else if (f < 0.6F) {
                itemstack = new ItemStack(Items.WHEAT);
            } else if (f < 0.8F) {
                itemstack = new ItemStack(Items.LEATHER);
            } else {
                itemstack = new ItemStack(Items.FEATHER);
            }
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FOX_FOOD);
    }

    protected int calculateFallDamage(float p_28545_, float p_28546_) {
        return Mth.ceil((p_28545_ - 5.0F) * p_28546_);
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent == SoundEvents.FOX_SCREECH) {
            this.playSound(soundevent, 2.0F, this.getVoicePitch());
        } else {
            super.playAmbientSound();
        }
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundEvents.FOX_SLEEP;
        } else {
            if (!this.level().isDay() && this.random.nextFloat() < 0.1F) {
                List<Player> list = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(
                        16.0, 16.0, 16.0), EntitySelector.NO_SPECTATORS);
                if (list.isEmpty()) {
                    return SoundEvents.FOX_SCREECH;
                }
            }
            return SoundEvents.FOX_AMBIENT;
        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_28548_) {
        return SoundEvents.FOX_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            this.spawnAtLocation(itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.55F * this.getEyeHeight(),
                this.getBbWidth() * 0.4F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896)
                .add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    static {
        DATA_TYPE_ID = SynchedEntityData.defineId(FoxServant.class, EntityDataSerializers.INT);
        DATA_FLAGS_ID = SynchedEntityData.defineId(FoxServant.class, EntityDataSerializers.BYTE);
        DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(FoxServant.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(FoxServant.class, EntityDataSerializers.OPTIONAL_UUID);
    }

    public enum Type implements StringRepresentable {
        RED(0, "red"),
        SNOW(1, "snow");
        @SuppressWarnings("deprecation")
        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::getId, values(),
                ByIdMap.OutOfBoundsStrategy.ZERO);
        private final int id;
        private final String name;

        Type(int p_196658_, String p_196659_) {
            this.id = p_196658_;
            this.name = p_196659_;
        }

        public String getSerializedName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        public static Type byName(String p_28817_) {
            return CODEC.byName(p_28817_, RED);
        }

        public static Type byId(int p_28813_) {
            return BY_ID.apply(p_28813_);
        }

        public static Type byBiome(Holder<Biome> p_204063_) {
            return p_204063_.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED;
        }
    }
}