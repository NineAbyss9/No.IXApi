
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.base.Predicates;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

public class HeadHuntersCrossbow
extends ProjectileWeaponItem {
    public HeadHuntersCrossbow() {
        super(new Properties().durability(0).rarity(Rarity.RARE));
    }

    public InteractionResultHolder<ItemStack> use(Level var1, Player var2, InteractionHand var3) {
        List<LivingEntity> var4 = var1.getEntitiesOfClass(LivingEntity.class,
                var2.getBoundingBox().inflate(64), living -> MobUtils.hasLineOfSight(var2, living));
        for (LivingEntity var5 : var4) {
            if (!var1.isClientSide) {
                EntityEventHandler.wardenBoom(var2, var2.damageSources().indirectMagic(var2, var2),
                        var5, 64);
                var2.getCooldowns().addCooldown(this, 40);
                break;
            }
        }
        return super.use(var1, var2, var3);
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return Predicates.alwaysFalse();
    }

    public int getDefaultProjectileRange() {
        return 32;
    }
}
