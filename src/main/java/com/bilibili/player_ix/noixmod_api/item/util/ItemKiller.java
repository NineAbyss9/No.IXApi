
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemKiller
extends Item {
    public ItemKiller() {
        super(new Properties().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        Player player = p_41427_.getPlayer();
        if (player != null) {
            List<Entity> list = WorldUtil.entityList(Entity.class, player, 365, 365, 365);
            for (Entity entity : list) {
                if (entity instanceof ItemEntity item) {
                    item.discard();
                }
            }
        }
        return super.useOn(p_41427_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        List<Entity> list = WorldUtil.entityList(Entity.class, p_41433_, 365, 365, 365);
        for (Entity entity : list) {
            if (entity instanceof ItemEntity item) {
                item.discard();
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("info.noixmodapi.item_killer"));
    }
}
