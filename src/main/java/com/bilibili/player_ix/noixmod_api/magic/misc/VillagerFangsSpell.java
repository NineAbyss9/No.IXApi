
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VillagerFangsSpell
extends Spell
{
    public Type getSpellType()
    {
        return Type.OVERWORLD;
    }

    public float spellPower()
    {
        return 15.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        var possibleTarget = MobUtils.getSingleTarget(pLevel, pCaster, 15.0D, 15.0D,
                entity -> entity instanceof LivingEntity && MobUtils.canHurt((LivingEntity)entity, pCaster));
        LivingEntity $$0 = possibleTarget instanceof LivingEntity ? (LivingEntity)possibleTarget : null;
        if ($$0 == null) {
            return;
        }
        double $$1 = Math.min($$0.getY(), pCaster.getY());
        double $$2 = Math.max($$0.getY(), pCaster.getY()) + 2.0;
        float $$3 = (float)Mth.atan2($$0.getZ() - pCaster.getZ(), $$0.getX() - pCaster.getX());
        if (pCaster.distanceToSqr($$0) < 9.0) {
            for (int $$4 = 0;$$4 < 38;++$$4) {
                float $$5 = $$3 + $$4 * Maths.CLOSER_PI * 0.4f;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$5) * 1.5, pCaster.getZ() +
                        Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
            }
            for (int $$6 = 0;$$6 < 5;++$$6) {
                float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 2.0f / 8.0f + 1.2566371f;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$7) * 2.5, pCaster.getZ() +
                        Mth.sin($$7) * 2.5, $$1, $$2, $$7, 1);
            }
            for (int $$6 = 0;$$6 < 8;++$$6) {
                float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 3.0f / 8.0f + 2.2566371f;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$7) * 3.0, pCaster.getZ() +
                                Mth.sin($$7) * 3.0,
                        $$1, $$2, $$7, 2);
            }
            for (int $$6 = 0;$$6 < 11;++$$6) {
                float $$7 = $$3 + (float)$$6 * (float)Math.PI * 4.0f / 8.0f + 3.2566371f;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$7) * 3.5, pCaster.getZ() + Mth.sin($$7) * 3.5,
                        $$1, $$2, $$7, 3);
            }
            for (int $$6 = 0;$$6 < 14;++$$6) {
                float $$7 = $$3 + $$6 * Maths.CLOSER_PI * 5.0f / 8.0f + 4.2566371f;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$7) * 4.0, pCaster.getZ() + Mth.sin($$7)
                        * 4.0, $$1, $$2, $$7, 4);
            }
        } else {
            float radius = 0.25f;
            for (int $$8 = 0;$$8 < 30;++$$8) {
                double $$9 = 1.25 * (double)($$8 + 1);
                float left = $$3 + radius;
                float right = $$3 - radius;
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos($$3) * $$9,
                        pCaster.getZ() + Mth.sin($$3) * $$9, $$1, $$2, $$3, $$8);
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos(left) * $$9, pCaster.getZ() +
                        Mth.sin(left) * $$9, $$1, $$2, left, $$8);
                this.createSpellEntity(pLevel, pCaster, pCaster.getX() + Mth.cos(right) * $$9, pCaster.getZ() +
                        Mth.sin(right) * $$9, $$1, $$2, right, $$8);
            }
        }
    }

    private void createSpellEntity(Level level, LivingEntity pOwner, double $$0, double $$1, double $$2, double $$3, float $$4, int $$5)
    {
        BlockPos $$6 = BlockPos.containing($$0, $$3, $$1);
        boolean $$7 = false;
        double $$8 = 0.0;
        do {
            VoxelShape $$12;
            BlockPos $$9 = $$6.below();
            BlockState $$10 = level.getBlockState($$9);
            if (!$$10.isFaceSturdy(level, $$9, Direction.UP)) continue;
            if (!level.isEmptyBlock($$6) && !($$12 = level.getBlockState($$6)
                    .getCollisionShape(level, $$6)).isEmpty()) {
                $$8 = $$12.max(Direction.Axis.Y);
            }
            $$7 = true;
            break;
        } while (($$6 = $$6.below()).getY() >= Mth.floor($$2) - 1);
        if ($$7) {
            VillagerFangs trap = new VillagerFangs(NoixmodAPIEntities.VILLAGER_FANGS.get(), level);
            trap.setPos($$0, $$6.getY() + $$8, $$1);
            trap.setYRot($$4 * 57.295776F);
            trap.setWarmupDelayTicks($$5);
            trap.setOwner(pOwner);
            level.addFreshEntity(trap);
        }
    }
}
