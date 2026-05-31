
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class UninvitedSword
extends ApiSword {
    public UninvitedSword() {
        super(1990, 2f, 4f, 1, 15, Ingredient.of(Items.SCULK_CATALYST),
                4, -2f, new Properties().fireResistant().stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player caster, InteractionHand p_41434_) {
        if (this.spawnPotion(p_41432_, caster)) {
            RandomSource source = p_41432_.getRandom();
            double x = caster.getX() + Mth.randomBetween(source, -6, 6);
            double z = caster.getZ() + Mth.randomBetween(source, -6, 6);
            for (int i = 0; i < 3;i++) {
                if (caster.randomTeleport(x, caster.getY(), z, true)) {
                    break;
                }
            }
            caster.getCooldowns().addCooldown(this, 300);
            return ItemUtils.startUsingInstantly(p_41432_, caster, p_41434_);
        }
        return InteractionResultHolder.fail(caster.getItemInHand(p_41434_));
    }

    private boolean spawnPotion(Level level, LivingEntity entity) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, entity.getX(), entity.getY(), entity.getZ());
        cloud.setOwner(entity);
        cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Maths.toTick(10), 0));
        cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Maths.toTick(3), 0));
        cloud.setDuration(Maths.toTick(10));
        return level.addFreshEntity(cloud);
    }
}
