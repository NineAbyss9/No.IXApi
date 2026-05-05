
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
    public static final String USE_COUNT = "NumberOfUses";
    public static final int MAX_USE_COUNT = 3;
    public OminousHorn() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.TOOT_HORN;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    public static boolean shouldDiscard(ItemStack stack)
    {
        var tag = stack.getOrCreateTag();
        int current = 0;
        if (tag.contains(USE_COUNT)) {
            current = tag.getInt(USE_COUNT);
        }
        return current > MAX_USE_COUNT;
    }

    public static void increaseUseCount(ItemStack stack)
    {
        var tag = stack.getOrCreateTag();
        int current = 0;
        if (tag.contains(USE_COUNT)) {
            current = tag.getInt(USE_COUNT);
        }
        tag.putInt(USE_COUNT, current + 1);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.getCooldowns().addCooldown(this, 60);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (pPlayer instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.RECORDS,
                    player.getX(), player.getY(), player.getZ(), 98F, 1f, player.getRandom().nextLong()));
        }
        if (pPlayer.isCrouching() && !pLevel.isClientSide) {
            NeoIllager illager = NoixmodAPIEntities.NEO_ILLAGER.get().create(pLevel);
            if (illager != null) {
                illager.setOwner(pPlayer);
                illager.moveTo(pPlayer.blockPosition(), 0, 0);
                WorldUtil.nullableFinalizeSpawn(illager, pLevel, pPlayer.blockPosition(), MobSpawnType.EVENT);
                if (pLevel.addFreshEntity(illager)) {
                    if (!pPlayer.isCreative()) {
                        increaseUseCount(stack);
                        if (shouldDiscard(stack)) {
                            stack.shrink(1);
                        }
                    }
                } else {
                    illager.discard();
                }
            }
        }
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }
}
