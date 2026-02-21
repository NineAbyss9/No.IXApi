
package com.bilibili.player_ix.noixmod_api.register.event;

import com.github.NineAbyss9.ix_api.api.Synchronizer;
import net.minecraftforge.eventbus.api.Event;

public class SyncEvent
extends Event {
    private final Synchronizer synchronizer;
    public SyncEvent(Synchronizer pSynchronizer) {
        super();
        synchronizer = pSynchronizer;
    }

    public Synchronizer getSynchronizer() {
        return synchronizer;
    }
}
