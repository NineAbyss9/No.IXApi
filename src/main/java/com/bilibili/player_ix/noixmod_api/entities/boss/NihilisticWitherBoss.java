
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.bilibili.player_ix.noixmod_api.world.ApiSavedData;
import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticWither;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NihilisticWitherBoss
extends NihilisticWither
implements ApiNihilisticBoss {
    public NihilisticWitherBoss(EntityType<NihilisticWitherBoss> type, Level level) {
        super(type, level);
        this.xpReward = 500;
        this.setHostile(true);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getInvulnerableTicks() <= 0) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    public void startSeenByPlayer(ServerPlayer p_31483_) {
        super.startSeenByPlayer(p_31483_);
        this.bossEvent.addPlayer(p_31483_);
    }

    public void stopSeenByPlayer(ServerPlayer p_31488_) {
        super.stopSeenByPlayer(p_31488_);
        this.bossEvent.removePlayer(p_31488_);
    }

    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        if (this.level().isClientSide || !(pDamageSource.getEntity() instanceof Player)) {
            return;
        }
        ApiSavedData.get(this.serverLevel()).setNihilisticWitherKilled();
    }

    public void setOwner(@Nullable LivingEntity lie) {
    }

    @Nullable
    public LivingEntity getOwner() {
        return null;
    }

    public boolean isOwned() {
        return false;
    }

    public boolean isOwnedBy(LivingEntity living) {
        return false;
    }

    public boolean isBoss() {
        return true;
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                true, this::canAttack));
    }
}
