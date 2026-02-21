
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IAgeableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.APIBreedGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MushroomSpider
extends AbstractSpiderServant {
    private int inLoveTime = 0;
    private Color spiderColor;
    public MushroomSpider(EntityType<? extends MushroomSpider> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setMaxUpStep(2F);
        this.setSpiderColor(Maths.trueOrFalse());
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SpiderAttackGoal(this, 0.8));
        this.goalSelector.addGoal(2, new APIBreedGoal(this, 0.6));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.7f));
        this.addBehaviorGoal(4, 0.7, 10F, true, true);
        this.addTargetGoal();
    }

    public Color getSpiderColor() {
        return this.spiderColor;
    }

    public void setSpiderColor(int i) {
        if (i == 1) {
            this.spiderColor = Color.BROWN;
        } else if (i == -1) {
            this.spiderColor = Color.RED;
        } else {
            this.spiderColor = Color.BOTH;
        }
    }

    public int getInLoveTime() {
        return this.inLoveTime;
    }

    public boolean isInLove() {
        return this.getInLoveTime() > 0;
    }

    public void setInLoveTime(int time) {
        this.inLoveTime = time;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("InLoveTime", this.getInLoveTime());
        tag.putInt("SpiderColor", this.getSpiderColor().id);
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("InLoveTime")) {
            this.setInLoveTime(tag.getInt("InLoveTime"));
        }
        if (tag.contains("SpiderColor")) {
            this.setSpiderColor(tag.getInt("SpiderColor"));
        }
        super.readAdditionalSaveData(tag);
    }

    public boolean isFood(ItemStack stack) {
        if (this.getSpiderColor() == Color.BROWN) {
            return stack.is(Items.BROWN_MUSHROOM);
        } else if (this.getSpiderColor() == Color.RED) {
            return stack.is(Items.RED_MUSHROOM);
        } else {
            return stack.is(Items.RED_MUSHROOM) || stack.is(Items.BROWN_MUSHROOM);
        }
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        Item item = stack.getItem();
        int t = pPlayer.isCreative() ? 0 : 1;
        if (item == Items.SPIDER_EYE) {
            if (this.isUnowned()) {
                this.setOwner(pPlayer);
                stack.shrink(t);
                if (!this.level().isClientSide()) {
                    ParticleUtil.addParticleAroundSelf(this, ParticleTypes.HEART, 12);
                }
                return InteractionResult.SUCCESS;
            }
        } else if (this.isFood(stack)) {
            this.setInLoveTime(Maths.toTick(8));
            this.heal(3F);
            stack.shrink(t);
            return InteractionResult.SUCCESS;
        } else if (stack.is(ItemTags.FLOWERS)) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED));
            stack.shrink(t);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public IAgeableMob getBreedMob() {
        return new MushroomSpider(NoixmodAPIEntities.MUSHROOM_SPIDER.get(), this.level());
    }

    public void spawnChildFromBreeding(ServerLevel p_27564_, IAgeableMob p_27565_) {
        MushroomSpider ageablemob = (MushroomSpider)this.getBreedMob();
        if (ageablemob != null) {
            ageablemob.setBaby(true);
            ageablemob.moveTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            ageablemob.finalizeSpawn(p_27564_, p_27564_.getCurrentDifficultyAt(this.blockPosition()),
                    MobSpawnType.BREEDING);
            p_27564_.addFreshEntityWithPassengers(ageablemob);
        }
    }

    public enum Color {
        BOTH(0),
        BROWN(1),
        RED(2);

        public final int id;

        Color(int i) {
            this.id = i;
        }
    }
}
