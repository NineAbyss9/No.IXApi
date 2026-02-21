
package com.bilibili.player_ix.noixmod_api.item;

import com.bilibili.player_ix.noixmod_api.entities.servant.illager.NeoIllager;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class OminousHorn
extends Item {
    public OminousHorn() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.TOOT_HORN;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    public InteractionResultHolder<ItemStack> use( Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        p_41433_.getCooldowns().addCooldown(this, 60);
        p_41433_.awardStat(Stats.ITEM_USED.get(this));
        if (p_41433_ instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.RECORDS,
                    player.getX(), player.getY(), player.getZ(), 98F, 1f, player.getRandom().nextLong()));
        }
        if (p_41433_.isCrouching() && !p_41432_.isClientSide) {
            NeoIllager illager = NoixmodAPIEntities.NEO_ILLAGER.get().create(p_41432_);
            if (illager != null) {
                illager.setOwner(p_41433_);
                illager.moveTo(p_41433_.blockPosition(), 0, 0);
                WorldUtil.nullableFinalizeSpawn(illager, p_41432_, p_41433_.blockPosition(), MobSpawnType.EVENT);
                if (!p_41432_.addFreshEntity(illager))
                    illager.discard();
            }
        }
        return InteractionResultHolder.consume(p_41433_.getItemInHand(p_41434_));
    }
}
