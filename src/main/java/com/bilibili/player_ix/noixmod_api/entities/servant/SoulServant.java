
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.SpellCasterMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SoulServant
extends OwnableMob
implements ApiRangedAttackMob, SpellCasterMob {
    private ApiSpells.ApiSpell currentSpell = ApiSpells.ApiSpell.NONE;
    public final OwnerSummon ownerSummon = new OwnerSummon(this);
    protected int spellTicks;

    public SoulServant(EntityType<? extends SoulServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public int getSpellTick() {
        return spellTicks;
    }

    public void setSpellType(ApiSpells.ApiSpell spell) {
        this.currentSpell = spell;
    }

    public void setSpellTick(int tick) {
        this.spellTicks = tick;
    }

    public SoundEvent getCastSound() {
        return SoundEvents.BELL_RESONATE;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this, 0.75, 10,
                30F));
        this.addBehaviorGoal(4, 0.6, 10F, true, true);
        this.addTargetGoal();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }
    }

    public boolean isCastingSpell() {
        return this.spellTicks > 0;
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        return new Arrow(EntityType.ARROW, this.level());
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack stack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item == Items.BOW));
        double[] doubles = this.ownerSummon.projectileDouble(livingEntity);
        double x = doubles[0];
        double y = doubles[1];
        double z = doubles[2];
        Arrow arrow = (Arrow)this.getArrow(stack, v);
        arrow.setCritArrow(this.random.nextBoolean());
        arrow.setEffectsFromItem(stack);
        arrow.moveTo(this.getX(), this.getY() + 1, this.getZ());
        arrow.setDeltaMovement(new Vec3(x, y, z));
        this.level().addFreshEntity(arrow);
    }

    protected static class AttackSpellGoal
    extends AbstractUseSpellGoal {
        protected final SoulServant servant;

        public AttackSpellGoal(SoulServant soul) {
            super(soul);
            this.servant = soul;
        }

        public boolean canUse() {
            return this.servant.isCastingSpell() && this.checkTarget();
        }

        protected void castSpell() {

        }

        protected int getCastingTime() {
            return 30;
        }

        protected int getCastingInterval() {
            return 300;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.ATTACK;
        }
    }
}
