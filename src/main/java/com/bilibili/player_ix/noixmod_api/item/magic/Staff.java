
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public abstract class Staff
extends Item {
    public Staff(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        if (checkConditions(pLevel, pPlayer)) {
            if (!pLevel.isClientSide) {
                this.prepareSpellCasting(((ServerLevel)pLevel), pPlayer);
            }
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    public int getUseDuration(ItemStack pStack)
    {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack pStack)
    {
        return UseAnim.BOW;
    }

    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration)
    {
        if (pLevel.isClientSide) {
            ParticleUtil.addParticle(pLevel, this.getParticleOptions(),
                    pLivingEntity.position().add(0.0D, pLivingEntity.getBoundingBox().maxY + 0.1D, 0.0D),
                    this.getSpellColor());
        }
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity)
    {
        if (!pLevel.isClientSide) {
            this.castSpell((ServerLevel)pLevel, pLivingEntity, 40);
        }
        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 40);
        }
        return pStack;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged)
    {
        if (!pLevel.isClientSide) {
            int i = pStack.getUseDuration() - pTimeCharged;
            if (i >= 30) {
                this.castSpell((ServerLevel)pLevel, pLivingEntity, i);
            }
        }
        if (pLivingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(this, 40);
        }
    }

    public boolean checkConditions(Level pLevel, LivingEntity pEntity)
    {
        return true;
    }

    public ParticleOptions getParticleOptions()
    {
        return ParticleTypes.ENTITY_EFFECT;
    }

    public float[] getSpellColor()
    {
        return new float[]{1.0F, 1.0F, 1.0F};
    }

    public void prepareSpellCasting(ServerLevel pLevel, LivingEntity pEntity) {
        pEntity.playSound(SoundEvents.EVOKER_PREPARE_ATTACK);
    }

    public abstract void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime);
}
