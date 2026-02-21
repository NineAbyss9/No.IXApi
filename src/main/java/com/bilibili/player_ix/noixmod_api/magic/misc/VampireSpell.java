
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class VampireSpell
extends Spell {
    @Override
    public Type getSpellType() {
        return Type.MISC;
    }

    @Override
    public float spellPower() {
        return 20;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, pCaster.getBoundingBox().inflate(128),
                living -> MobUtils.canHurt(living, pCaster));
        if (!list.isEmpty()) {
            boolean flag = true;
            for (LivingEntity living : list) {
                if (MobUtils.hasLineOfSight(pCaster, living)) {
                    EntityEventHandler.broadcastEntityEvent(living, 4);
                    living.hurt(pCaster.damageSources().indirectMagic(pCaster, pCaster), 6f);
                    ItemStack stack = pCaster.getOffhandItem();
                    if (stack.is(Items.GLASS_BOTTLE)) {
                        stack.shrink(1);
                        if (pCaster instanceof Player player) {
                            if (player.getOffhandItem().isEmpty()) {
                                player.setItemInHand(InteractionHand.OFF_HAND,
                                        new ItemStack(NoixmodAPIItems.BLOOD_BOTTLE.get()));
                            } else {
                                player.addItem(new ItemStack(NoixmodAPIItems.BLOOD_BOTTLE.get()));
                            }
                        }
                    }
                    living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.DESIRE_FOR_BLOOD.get(),
                            400, 0));
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (LivingEntity living : list) {
                    living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.DESIRE_FOR_BLOOD.get(),
                            600, 0));
                    break;
                }
            }
        }
    }
}
