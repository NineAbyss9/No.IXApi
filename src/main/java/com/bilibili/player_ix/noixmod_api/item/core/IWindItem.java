
package com.bilibili.player_ix.noixmod_api.item.core;

import com.bilibili.player_ix.noixmod_api.register.ApiEnchantments;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IWindItem
{
    default void fly(Level pLevel, Player pPlayer, InteractionHand pHand)
    {
        this.fly(pLevel, pPlayer, pHand, 1);
    }

    default void fly(Level pLevel, Player pPlayer, InteractionHand pHand, int pLevelToIgnoreBreaking)
    {
        Vec3 lookAngle = pPlayer.getLookAngle();
        ItemStack stack = pPlayer.getItemInHand(pHand);
        int i = stack.getEnchantmentLevel(ApiEnchantments.POTENT.get());
        if (!pLevel.isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)pLevel, ParticleTypes.CLOUD, pPlayer.position(),
                    10 + Math.min(i, 10), 0.8D, 0.8D, 0.8D, 0.0D);
            pPlayer.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
        }
        double speed = 3.0 * Math.max(i, 1);
        Vec3 vec3 = new Vec3(lookAngle.x * speed, lookAngle.y, lookAngle.z * speed);
        pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(vec3));
        pPlayer.resetFallDistance();
        int level = stack.getEnchantmentLevel(Enchantments.UNBREAKING);
        if (level < pLevelToIgnoreBreaking) {
            pPlayer.getMainHandItem().hurtAndBreak(1, pPlayer, player -> {
                player.broadcastBreakEvent(pHand);
            });
        }
    }

    default void flyUp(Level pLevel, LivingEntity pPlayer, InteractionHand pHand)
    {
        this.flyUp(pLevel, pPlayer, pHand, 3);
    }

    default void flyUp(Level pLevel, LivingEntity pPlayer, InteractionHand pHand, int pLevelToIgnoreBreaking)
    {
        //Vec3 lookAngle = pPlayer.getLookAngle();
        ItemStack stack = pPlayer.getItemInHand(pHand);
        int i = stack.getEnchantmentLevel(ApiEnchantments.POTENT.get());
        if (!pLevel.isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)pLevel, ParticleTypes.CLOUD, pPlayer.position(),
                    10 + Math.min(i, 10), 0.8D, 0.8D, 0.8D, 0.0D);
            pPlayer.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
        }
        double speed = 3.0D * Math.max(i, 1);
        //Vec3 vec3 = new Vec3(lookAngle.x * speed, lookAngle.y, lookAngle.z * speed);
        pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(0.0D, speed, 0.0D));
        if (pPlayer instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)pPlayer;
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
        //pPlayer.hasImpulse = true;
        pPlayer.resetFallDistance();
        int level = stack.getEnchantmentLevel(Enchantments.UNBREAKING);
        if (level < pLevelToIgnoreBreaking) {
            pPlayer.getMainHandItem().hurtAndBreak(1, pPlayer, player -> {
                player.broadcastBreakEvent(pHand);
            });
        }
    }
}
