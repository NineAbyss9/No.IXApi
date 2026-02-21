
package com.github.NineAbyss9.ix_api.ix_api.api.item;

import net.minecraft.world.item.Item;

public class BaseItem extends Item {
    public BaseItem(Properties properties) {
        super(properties);
    }

    public BaseItem() {
        this(new Properties().stacksTo(64));
    }
}
