
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**Tracker class.A human-like who was looking at you.*/
public class Tracker
extends AbstractHorrorMob {
    private boolean playerLooked;
    private final int actNameId;
    private Component actName;
    private static final EntityDataAccessor<Integer> DATA_LIFE;
    public Tracker(EntityType<? extends Tracker> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        int actMobId = p_33003_.random.nextInt(4);
        this.actNameId = actMobId;
        this.actName = this.getActName(actMobId);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LIFE, 6000);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public void aiStep() {
        super.aiStep();
        if (getLife() > 0)
            setLife(getLife() - 1);
        else {
            die();
            discard();
        }
        LivingEntity target = this.getTarget();
        if (target instanceof Player player) {
            this.lookControl.setLookAt(player, 30.0F, 30.0F);
            if (isLookingAtMe(player))
                this.playerLooked = true;
            if (closerThan(target, 60.0D)) {
                this.getNavigation().stop();
            } else {
                this.getNavigation().moveTo(target, 1.0D);
            }
        }
    }

    public Component getDisplayName() {
        return this.playerLooked ? super.getDisplayName() : this.actName;
    }

    public Component getName() {
        return this.getDisplayName();
    }

    boolean isLookingAtMe(Player pPlayer) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ()
                - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - 0.025D / d0 && pPlayer.hasLineOfSight(this);
    }

    public void die() {
        if (this.isServerSide()) {
            ParticleUtil.sendParticles(this.serverLevel(), ParticleTypes.LARGE_SMOKE, this.position(), 5,
                    0.15, 0.5, 0.15, 0.05);
        }
    }

    public Component getActName(int actMobId) {
        return switch (actMobId) {
            case 0 -> Component.translatable("entity.minecraft.pig");
            case 1 -> Component.translatable("entity.minecraft.sheep");
            case 2 -> Component.translatable("entity.minecraft.cow");
            case 3 -> Component.translatable("entity.minecraft.chicken");
            default -> Component.translatable("entity.minecraft.horse");
        };
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.actNameId);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.actName = this.getActName(pPacket.getData());
    }

    public int getLife() {
        return this.entityData.get(DATA_LIFE);
    }

    public void setLife(int pLife) {
        this.entityData.set(DATA_LIFE, pLife);
    }

    protected void playStepSound(BlockPos pPos, BlockState pState) {}

    protected void playBlockFallSound() {}

    protected void playSwimSound(float pVolume) {}

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        super.actuallyHurt(p_21240_, p_21241_ / 50F);
    }

    public static AttributeSupplier createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MAX_HEALTH, 40).build();
    }

    static {
        DATA_LIFE = SynchedEntityData.defineId(Tracker.class, EntityDataSerializers.INT);
    }
}
