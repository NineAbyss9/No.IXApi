
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Wall
extends OwnedEntity {
    private ResourceLocation texture;
    public Wall(EntityType<? extends Wall> type, Level level) {
        super(type, level);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(ResourceLocation location) {
        this.texture = location;
    }
}
