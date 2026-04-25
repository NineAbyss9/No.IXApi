
package com.bilibili.player_ix.noixmod_api.entities.projectile.arrow;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ArrowArrowEntity
extends Arrow {
    public ArrowArrowEntity(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ArrowArrowEntity(Level pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    public ArrowArrowEntity(Level pLevel, LivingEntity pShooter) {
        super(pLevel, pShooter);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.ARROW_ARROW.get();
    }

    public static ArrowArrowEntity simple(Level pLevel, LivingEntity pShooter) {
        var entity = new ArrowArrowEntity(NoixmodAPIEntities.ARROW_ARROW.get(), pLevel);
        entity.setOwner(pShooter);
        entity.setPos(pShooter.getX(), pShooter.getEyeY() - 0.1D, pShooter.getZ());
        if (pShooter instanceof Player) {
            entity.pickup = AbstractArrow.Pickup.ALLOWED;
        }
        return entity;
    }

    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        for (int i = 0; i < 6; ++i) {
            Arrow arrow = new Arrow(EntityType.ARROW, this.level());
            arrow.moveTo(this.blockPosition().offset(Maths.randomInt(2), 5, Maths.randomInt(2)),
                    0, 0);
            arrow.setOwner(this.getOwner());
            this.level().addFreshEntity(arrow);
        }
    }
}
