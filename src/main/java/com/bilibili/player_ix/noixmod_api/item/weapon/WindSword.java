
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.register.ApiEnchantments;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WindSword
extends ApiSword {
    public WindSword() {
        super(1024, 5.5F, 3.0F, 3, 11, Ingredient.of(
                NoixmodAPIItems.WIND_ESSENCE.get()), 4, -2.4F,
                new Properties().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Vec3 lookAngle = pPlayer.getLookAngle();
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        int i = stack.getEnchantmentLevel(ApiEnchantments.POTENT.get());
        if (!pLevel.isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)pLevel, ParticleTypes.CLOUD, pPlayer.position(),
                    10 + Math.min(i, 10), 0.8, 0.8, 0.8, 0);
            pPlayer.playSound(SoundEvents.ILLUSIONER_MIRROR_MOVE);
        }
        double speed = 3.0 * Math.max(i, 1);
        Vec3 vec3 = new Vec3(lookAngle.x * speed, lookAngle.y, lookAngle.z * speed);
        pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(vec3));
        pPlayer.resetFallDistance();
        int level = stack.getEnchantmentLevel(Enchantments.UNBREAKING);
        if (level <= 0) {
            pPlayer.getMainHandItem().hurtAndBreak(1, pPlayer, player ->
                    player.broadcastBreakEvent(pUsedHand));
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
