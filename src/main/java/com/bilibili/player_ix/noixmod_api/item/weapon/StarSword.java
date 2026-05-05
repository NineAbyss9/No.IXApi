
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
        super(0, 5F, 11, 9, 12, Ingredient.EMPTY, 1,
                -2.3F, new Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1));
    }

    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity player) {
        player.heal(1F);
        return true;
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        pTooltipComponents.add(Component.translatable("info.noixmodapi.star_sword"));
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        List<Apostle> bosses = p_41432_.getEntitiesOfClass(Apostle.class, p_41433_.getBoundingBox()
                .inflate(99, 3,  99), apostle -> apostle.getOwner() != p_41433_);
        if (!bosses.isEmpty()) {
            p_41433_.getCooldowns().addCooldown(this, 600);
            for (Apostle boss : bosses) {
                boss.handleAfraid();
                if (boss.isShadow()) {
                    boss.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
        List<PowerEntity> powers = p_41432_.getEntitiesOfClass(PowerEntity.class, p_41433_.getBoundingBox()
                        .inflate(66), power -> power.getOwner() != p_41433_);
        if (!powers.isEmpty()) {
            for (PowerEntity entity : powers) {
                entity.hurt(p_41432_.damageSources().playerAttack(p_41433_), 30f);
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }
}
