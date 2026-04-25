
package com.bilibili.player_ix.noixmod_api.entities.projectile.arrow;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

public class VampireArrow
extends ArrowArrowEntity {
    public VampireArrow(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
        this.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.VAMPIRE.get(), Maths.toTick(10), 0));
    }

    public VampireArrow(Level level, LivingEntity living) {
        super(level, living);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.VAMPIRE_ARROW.get();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onHitEntity(EntityHitResult p_36757_) {
        Entity entity = p_36757_.getEntity();
        if (this.getOwner() instanceof LivingEntity living) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(
                        new MobEffectInstance(NoixmodAPIMobEffects.VAMPIRE.get(), Maths.toTick(10), 0));
                if (MobUtils.canHurt(livingEntity, living)) {
                    living.heal(1f);
                }
            }
        }
        super.onHitEntity(p_36757_);
    }
}
