
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.projectile.PowerEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarSword extends ApiSword {
    public StarSword() {
        super(0, 5F, 8.0F, 9, 12, Ingredient.EMPTY, 1,
                -2.4F, new Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(1F);
        }
        return true;
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        pTooltipComponents.add(Component.translatable("info.noixmodapi.star_sword"));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        List<Apostle> bosses = pLevel.getEntitiesOfClass(Apostle.class, pPlayer.getBoundingBox()
                .inflate(99, 3,  99), apostle -> apostle.getOwner() != pPlayer);
        if (!bosses.isEmpty()) {
            pPlayer.getCooldowns().addCooldown(this, 600);
            for (Apostle boss : bosses) {
                boss.handleAfraid();
                if (boss.isShadow()) {
                    boss.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
        List<PowerEntity> powers = pLevel.getEntitiesOfClass(PowerEntity.class, pPlayer.getBoundingBox()
                        .inflate(66), power -> power.getOwner() != pPlayer);
        if (!powers.isEmpty()) {
            for (PowerEntity entity : powers) {
                entity.hurt(pLevel.damageSources().playerAttack(pPlayer), 30f);
            }
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
