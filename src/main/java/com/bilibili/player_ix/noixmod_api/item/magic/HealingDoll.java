
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.github.NineAbyss9.ix_api.api.item.BaseItem;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.servant.Healing;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class HealingDoll
extends BaseItem {
    public HealingDoll() {
        super();
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (!p_41432_.isClientSide) {
            Healing healing = NoixmodAPIEntities.HEALING.get().create(p_41432_);
            if (healing != null) {
                healing.moveTo(p_41433_.position().add(Maths.randomInt(2), 0, Maths.randomInt(2)));
                healing.setOwner(p_41433_);
                p_41432_.addFreshEntity(healing);
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }
}
