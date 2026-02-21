
package com.bilibili.player_ix.noixmod_api.item.weapon;

import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NihilisticFireballItem
extends Item {
    public NihilisticFireballItem() {
        super(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON).fireResistant());
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 10;
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        Vec3 vec3 = p_41411_.getViewVector(1F);
        NihilisticFireball fireball = new NihilisticFireball(
                p_41411_.getX() + vec3.x / 2,
                p_41411_.getEyeY() - 0.2, p_41411_.getZ() + vec3.z / 2,
                vec3.x, vec3.y, vec3.z, p_41410_
        );
        fireball.setOwner(p_41411_);
        ItemUtil.shrink(p_41409_, p_41411_);
        p_41410_.addFreshEntity(fireball);
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }
}
