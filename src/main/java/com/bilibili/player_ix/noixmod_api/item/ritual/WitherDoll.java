
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticWitherBoss;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticWither;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.world.ApiSavedData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WitherDoll
extends Item {
    public WitherDoll() {
        super(new Item.Properties().rarity(Rarity.RARE));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            pPlayer.playSound(SoundEvents.EVOKER_CAST_SPELL);
        } else {
            boolean flag = false;
            if (pLevel.dimension() == Level.NETHER) {
                WitherBoss boss = EntityType.WITHER.create(pLevel);
                if (boss != null) {
                    boss.moveTo(pPlayer.position());
                    boss.makeInvulnerable();
                    flag = pLevel.addFreshEntity(boss);
                }
            } else {
                ServerLevel level = (ServerLevel)pLevel;
                boolean killed = ApiSavedData.get(level).isNihilisticWitherKilled();
                if (killed && pPlayer.isCrouching()) {
                    if (killed && level.getEntitiesOfClass(NihilisticWither.class,
                                    pPlayer.getBoundingBox().inflate(64.0D),
                                    e -> e.isOwnedBy(pPlayer)).isEmpty()) {
                        NihilisticWither wither = NoixmodAPIEntities.NIHILISTIC_WITHER.get().create(pLevel);
                        if (wither != null) {
                            wither.moveTo(pPlayer.position());
                            wither.finalizeSpawn(level, level.getCurrentDifficultyAt(pPlayer.blockPosition()),
                                    MobSpawnType.EVENT);
                            wither.setOwner(pPlayer);
                            if (level.addFreshEntity(wither))
                            {
                                pPlayer.getCooldowns().addCooldown(this, 400);
                                return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
                            } else {
                                wither.discard();
                            }
                        }
                    } else {
                        ServerPlayer player = (ServerPlayer)pPlayer;
                        player.connection.send(new ClientboundSetActionBarTextPacket(!killed ?
                                Component.translatable(
                                "info.noixmodapi.summon_nihilistic_wither.failed") :
                                Component.translatable("info.noixmodapi.servant.too_many")));
                        flag = true;
                    }
                } else {
                    NihilisticWitherBoss wither = NoixmodAPIEntities.NIHILISTIC_WITHER_BOSS.get().create(pLevel);
                    if (wither != null) {
                        wither.moveTo(pPlayer.position());
                        wither.finalizeSpawn(level, level.getCurrentDifficultyAt(pPlayer.blockPosition()),
                                MobSpawnType.EVENT);
                        flag = level.addFreshEntity(wither);
                    }
                }
            }
            if (flag) {
                pPlayer.getCooldowns().addCooldown(this, 400);
            }
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
