
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DamageTester extends ApiPathfinderMob {
    private static final String DAMAGE = "Damage:";
    public DamageTester(EntityType<DamageTester> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }
    private String give(String st) {
        return DAMAGE + st;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("hurt " + pSource.type() + " " + pAmount)));
        }
        return false;
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("actuallyHurt " + p_21240_.type() + " " + p_21241_)));
        }
    }

    public void setHealth(float p_21154_) {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("setHealth " + p_21154_)));
        }
        super.setHealth(20 + 1.0E-1F);
    }

    public void kill() {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("kill")));
        }
    }

    public void die(DamageSource p_21014_) {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("die " + p_21014_.type())));
        }
    }

    public void remove(RemovalReason p_276115_) {
        if (!this.level().isClientSide) {
            this.sendSystemMessage(Component.literal(this.give("remove " + p_276115_)));
        }
    }
}
