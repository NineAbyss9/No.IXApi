
package com.bilibili.player_ix.noixmod_api.entities.projectile.arrow;

import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class ArrowArrowEntity
extends Arrow {
    public ArrowArrowEntity(EntityType<? extends Arrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    @SuppressWarnings("all")
    public ArrowArrowEntity(Level level, @Nullable LivingEntity living) {
        super(level, living);
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
