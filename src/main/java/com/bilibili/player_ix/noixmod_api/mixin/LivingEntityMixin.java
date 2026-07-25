
package com.bilibili.player_ix.noixmod_api.mixin;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.ApostleBoss;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, IForgeLivingEntity {
    @Shadow public abstract boolean hasEffect(MobEffect p_21024_);

    @Shadow public abstract float getHealth();

    private LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    public void setHealth(float p_21154_, CallbackInfo ci) {
        float delta = p_21154_ - this.getHealth();
        if (delta <= 0) {
            return;
        }
        List<ApostleBoss> bosses = this.level().getEntitiesOfClass(ApostleBoss.class, this.getBoundingBox()
                        .inflate(64), boss -> MobUtils.canHurt(this.self(), boss));
        if (bosses.isEmpty() || !NoixmodAPIMainConfig.HorrorMode.get()) {
            if (this.hasEffect(NoixmodAPIMobEffects.NIHILISTIC.get())) {
                ci.cancel();
            }
        } else {
            for (ApostleBoss boss : bosses) {
                if (boss != null) {
                    if (boss.getTarget() == this.self()) {
                        ci.cancel();
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    public void heal(float p_21154_, CallbackInfo ci) {
        List<ApostleBoss> bosses = this.level().getEntitiesOfClass(ApostleBoss.class, this.getBoundingBox()
                        .inflate(64), boss -> MobUtils.canHurt(this.self(), boss));
        if (!bosses.isEmpty() && NoixmodAPIMainConfig.HorrorMode.get()) {
            for (ApostleBoss boss : bosses) {
                if (boss != null) {
                    if (boss.getTarget() == this.self()) {
                        ci.cancel();
                        break;
                    }
                }
            }
            return;
        }
        if (this.hasEffect(NoixmodAPIMobEffects.NIHILISTIC.get())) {
            ci.cancel();
        }
    }
}
