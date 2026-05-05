
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class OwnableWormIllager
extends AbstractWormIllager
implements Ownable {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    public OwnableWormIllager(EntityType<? extends OwnableWormIllager> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Nullable
    public UUID getOwnerUUID() {
        if (this.owner == null && this.ownerUUID != null && !this.level().isClientSide) {
            Entity $$0 = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if ($$0 instanceof LivingEntity lie) {
                this.setOwner(lie);
            }
        }
        return this.ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
    }
}
