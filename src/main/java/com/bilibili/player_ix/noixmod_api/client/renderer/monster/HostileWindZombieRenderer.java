
package com.bilibili.player_ix.noixmod_api.client.renderer.monster;

import com.bilibili.player_ix.noixmod_api.client.renderer.WindZombieRenderer;
import com.bilibili.player_ix.noixmod_api.entities.monster.hostile.HostileWindZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HostileWindZombieRenderer<T extends HostileWindZombie>
extends WindZombieRenderer<T>
{
    public HostileWindZombieRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_);
    }
}
