
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractSkeletonServant;
import com.bilibili.player_ix.noixmod_api.magic.ISpell;
import com.bilibili.player_ix.noixmod_api.magic.Spells;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BoneStaff
extends Staff
{
    public BoneStaff(Properties properties)
    {
        super(properties);
    }

    public BoneStaff()
    {
        this(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public boolean checkConditions(Level pLevel, LivingEntity pEntity)
    {
        boolean flag = pLevel.getEntitiesOfClass(AbstractSkeletonServant.class,
                        pEntity.getBoundingBox().inflate(32), e -> e.getOwner() == pEntity)
                .size() < 6;
        if (!flag && pEntity instanceof ServerPlayer player) {
            player.connection.connection.send(new ClientboundSetActionBarTextPacket(
                    Component.translatable("info.noixmodapi.servant.too_many")));
        }
        return flag;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime)
    {
        ISpell spell = pLevel.dimension() == Level.NETHER ?
                Spells.WITHER_SKELETON.get() : Spells.SKELETON.get();
        spell.castSpell(pLevel, pCaster);
    }
}
