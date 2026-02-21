
package com.bilibili.player_ix.noixmod_api;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.PAMAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PAMAreNonnullByDefault
public class IXLib {
    private static final StackWalker STACK_WALKER;
    private static final Logger LOGGER = LoggerFactory.getLogger(IXLib.class);

    static {
        STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    }
}
