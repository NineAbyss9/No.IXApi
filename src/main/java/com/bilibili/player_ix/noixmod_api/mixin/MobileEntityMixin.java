
package com.bilibili.player_ix.noixmod_api.mixin;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobileEntityMixin extends LivingEntity implements Targeting {
    protected MobileEntityMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "getTarget", at = @At("RETURN"), cancellable = true)
    public void getTarget(CallbackInfoReturnable<LivingEntity> cir) {
        if (this.hasEffect(NoixmodAPIMobEffects.STUN.get())) {
            cir.setReturnValue(null);
        }
    }
}
