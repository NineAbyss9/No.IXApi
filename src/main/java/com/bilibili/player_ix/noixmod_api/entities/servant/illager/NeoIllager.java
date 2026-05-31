
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class NeoIllager
extends OwnableIllager {
    public NeoIllager(EntityType<? extends NeoIllager> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1.0D));
        this.addBehaviorGoal(5, 0.6D, 10F, true, false);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand)
    {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (!pPlayer.isCrouching() &&
                !stack.is(NoixmodAPIItems.NEO_ILLAGER_SPAWN_EGG.get()) &&
                this.getOwner() == pPlayer) {
            if (stack.is(NoixmodAPIItems.AXE_OF_HUNTER.get())) {
                HunterServant servant = NoixmodAPIEntities.HUNTER_SERVANT.get().create(this.level());
                this.copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, pPlayer);
            } else if (stack.getItem() instanceof AxeItem) {
                VindicatorServant servant = NoixmodAPIEntities.VINDICATOR_SERVANT.get().create(level());
                servant.setMainHandItem(stack.getItem());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, pPlayer);
            } else if (stack.is(Items.BOW)) {
                ArcherServant servant = NoixmodAPIEntities.ARCHER_SERVANT.get().create(level());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, pPlayer);
            } else if (stack.is(Items.TOTEM_OF_UNDYING)) {
                EvokerServant servant = NoixmodAPIEntities.EVOKER_SERVANT.get().create(level());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
            } else if (stack.is(Items.CROSSBOW)) {
                PillagerServant servant = NoixmodAPIEntities.PILLAGER_SERVANT.get().create(level());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, pPlayer);
            } else if (stack.is(NoixmodAPIItems.WINE.get())) {
                DrunkennessServant servant = NoixmodAPIEntities.DRUNKENNESS_SERVANT.get().create(level());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, pPlayer);
            } else if (stack.is(Items.BLUE_BANNER)) {
                IllusionerServant servant = NoixmodAPIEntities.ILLUSIONER_SERVANT.get().create(level());
                copyTo(servant);
                if (level().addFreshEntity(servant))
                    discard();
                else
                    servant.discard();
                return success(stack, servant);
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    /**Success and shrink*/
    private InteractionResult success(ItemStack stack, Entity pEntity) {
        ItemUtil.shrink(stack, pEntity);
        return InteractionResult.sidedSuccess(pEntity.level().isClientSide);
    }

    private void copyTo(OwnableIllager other) {
        other.setOwner(this.getOwner());
        other.setPos(this.position());
    }

    public ApiPose getPoses() {
        if (isAggressive())
            return ApiPose.ATTACKING;
        return ApiPose.CROSSED;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3499999940395355)
                .add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }
}
