
package com.bilibili.player_ix.noixmod_api.entities.servant.aquatic;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElderGuardianServant
extends GuardianServant {
    public static final float ELDER_SIZE_SCALE;
    public ElderGuardianServant(EntityType<? extends OwnableMob> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
        if (this.randomStrollGoal != null) {
            this.randomStrollGoal.setInterval(400);
        }
    }

    public int getAttackDuration() {
        return 60;
    }

    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_AMBIENT :
                SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
    }

    protected SoundEvent getHurtSound(DamageSource p_32468_) {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_HURT :
                SoundEvents.ELDER_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_DEATH :
                SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ELDER_GUARDIAN_FLOP;
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if ((this.tickCount + this.getId()) % 1200 == 0) {
            MobEffectInstance $$0 = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 6000, 2);
            List<ServerPlayer> $$1 = MobEffectUtil.addEffectToPlayersAround(this.serverLevel(),
                    this, this.position(), 50.0, $$0, 1200);
            $$1.forEach((player) ->
                    player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket
                    .GUARDIAN_ELDER_EFFECT, this.isSilent() ? 0.0F : 1.0F)));
        }
        if (!this.hasRestriction()) {
            this.restrictTo(this.blockPosition(), 16);
        }
    }

    static {
        ELDER_SIZE_SCALE = EntityType.ELDER_GUARDIAN.getWidth() / EntityType.GUARDIAN.getWidth();
    }
}
