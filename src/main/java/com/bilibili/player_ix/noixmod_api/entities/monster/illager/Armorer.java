
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.IXList;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.GoToLivingGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Armorer
extends AbstractIllager {
    public Armorer(EntityType<? extends Armorer> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 2;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_CHESTPLATE));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ArmorerGoalToLivingGoal(this, 1));
        OwnableMob.addBehaviorGoals(this, 4, 0.7, 10F, true, true);
    }

    public void tick() {
        super.tick();
        if (Math.random() < 0.1 && this.isAlive()) {
            List<AbstractIllager> illagers = this.level().getEntitiesOfClass(AbstractIllager.class, this.getBoundingBox()
                            .inflate(4), illager -> MobUtils.areAllies(illager, this) &&
                    !(illager instanceof Armorer) && !isArmorFilled(illager));
            if (!illagers.isEmpty()) {
                for (AbstractIllager illager : illagers) {
                    if (illager.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                        this.distribute(EquipmentSlot.HEAD, illager);
                        this.enchantArmor(illager, EquipmentSlot.HEAD);
                    } else if (illager.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                        this.distribute(EquipmentSlot.CHEST, illager);
                        this.enchantArmor(illager, EquipmentSlot.CHEST);
                    } else if (illager.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                        this.distribute(EquipmentSlot.LEGS, illager);
                        this.enchantArmor(illager, EquipmentSlot.LEGS);
                    } else if (illager.getItemBySlot(EquipmentSlot.FEET).isEmpty()) {
                        this.distribute(EquipmentSlot.FEET, illager);
                        this.enchantArmor(illager, EquipmentSlot.FEET);
                    }
                }
            }
        }
    }

    public void distribute(EquipmentSlot slot, LivingEntity living) {
        switch (slot) {
            case HEAD -> living.setItemSlot(EquipmentSlot.HEAD, this.getHeadItem());
            case CHEST -> living.setItemSlot(EquipmentSlot.CHEST, this.getChestItem());
            case LEGS -> living.setItemSlot(EquipmentSlot.LEGS, this.getLegsItem());
            case FEET -> living.setItemSlot(EquipmentSlot.FEET, this.getFeetItem());
        }
    }

    public ItemStack getHeadItem() {
        Integer integer = NoixmodAPIMainConfig.ArmorerDistributeArmorConfig.get();
        switch (integer) {
            case 1 -> {
                return new ItemStack(Items.GOLDEN_HELMET);
            }
            case 2 -> {
                return new ItemStack(Items.DIAMOND_HELMET);
            }
            case 3 -> {
                return new ItemStack(Items.NETHERITE_HELMET);
            }
            case 4 -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("advancednetherite:netherite_diamond_helmet"));
                if (item == null) {
                    return ItemStack.EMPTY;
                } else {
                    return item.getDefaultInstance();
                }
            }
            default -> {
                return new ItemStack(Items.IRON_HELMET);
            }
        }
    }

    public ItemStack getChestItem() {
        Integer integer = NoixmodAPIMainConfig.ArmorerDistributeArmorConfig.get();
        switch (integer) {
            case 1 -> {
                return new ItemStack(Items.GOLDEN_CHESTPLATE);
            }
            case 2 -> {
                return new ItemStack(Items.DIAMOND_CHESTPLATE);
            }
            case 3 -> {
                return new ItemStack(Items.NETHERITE_CHESTPLATE);
            }
            case 4 -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("advancednetherite:netherite_diamond_chestplate"));
                if (item == null) {
                    return ItemStack.EMPTY;
                } else {
                    return item.getDefaultInstance();
                }
            }
            default -> {
                return new ItemStack(Items.IRON_CHESTPLATE);
            }
        }
    }

    public ItemStack getLegsItem() {
        Integer integer = NoixmodAPIMainConfig.ArmorerDistributeArmorConfig.get();
        switch (integer) {
            case 1 -> {
                return new ItemStack(Items.GOLDEN_LEGGINGS);
            }
            case 2 -> {
                return new ItemStack(Items.DIAMOND_LEGGINGS);
            }
            case 3 -> {
                return new ItemStack(Items.NETHERITE_LEGGINGS);
            }
            case 4 -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("advancednetherite:netherite_diamond_leggings"));
                if (item == null) {
                    return ItemStack.EMPTY;
                } else {
                    return item.getDefaultInstance();
                }
            }
            default -> {
                return new ItemStack(Items.IRON_LEGGINGS);
            }
        }
    }

    public ItemStack getFeetItem() {
        Integer integer = NoixmodAPIMainConfig.ArmorerDistributeArmorConfig.get();
        switch (integer) {
            case 1 -> {
                return new ItemStack(Items.GOLDEN_BOOTS);
            }
            case 2 -> {
                return new ItemStack(Items.DIAMOND_BOOTS);
            }
            case 3 -> {
                return new ItemStack(Items.NETHERITE_BOOTS);
            }
            case 4 -> {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("advancednetherite:netherite_diamond_boots"));
                if (item == null) {
                    return ItemStack.EMPTY;
                } else {
                    return item.getDefaultInstance();
                }
            }
            default -> {
                return new ItemStack(Items.IRON_BOOTS);
            }
        }
    }

    public Enchantment getArmorEnchantment() {
        int integer = this.getRandom().nextInt(4);
        switch (integer) {
            case 1 -> {
                return Enchantments.FIRE_PROTECTION;
            }
            case 2 -> {
                return Enchantments.MENDING;
            }
            case 3 -> {
                return Enchantments.PROJECTILE_PROTECTION;
            }
            default -> {
                return Enchantments.ALL_DAMAGE_PROTECTION;
            }
        }
    }

    public void enchantArmor(LivingEntity entity, EquipmentSlot slot) {
        int level = NoixmodAPIMainConfig.ArmorerArmorEnchantLevel.get();
        if (level > 0) {
            entity.getItemBySlot(slot).enchant(this.getArmorEnchantment(), level);
        }
    }

    public static boolean isArmorFilled(LivingEntity entity) {
        if (entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            return false;
        } else if (entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            return false;
        } else if (entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
            return false;
        } else {
            return !entity.getItemBySlot(EquipmentSlot.FEET).isEmpty();
        }
    }

    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.ARMOR_EQUIP_IRON);
    }

    public void applyRaidBuffs(int i, boolean b) {}

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    public static void init() {
        if (NoixmodAPIMainConfig.ArmorerJoinRaid.get()) {
            Raid.RaiderType.create("armorer", NoixmodAPIEntities.ARMORER.get(),
                    IXList.raidCount(NoixmodAPIMainConfig.ArmorerRaidCount.get()));
        }
        MobUtils.registerSpawn(NoixmodAPIEntities.ARMORER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MobUtils::illagerSpawnPredicate);
    }

    private static class ArmorerGoalToLivingGoal extends GoToLivingGoal {
        public ArmorerGoalToLivingGoal(PathfinderMob finder, double speed) {
            super(finder, speed, AbstractIllager.class);
        }

        @Override
        public boolean canUse() {
            if (this.targetEntity instanceof Bugler || this.targetEntity instanceof Armorer) {
                return false;
            }
            return super.canUse();
        }
    }
}
