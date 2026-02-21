
package com.bilibili.player_ix.noixmod_api.register;

import org.slf4j.Logger;

/**This class provides some information in {@link Logger}*/
public final class ErrorCodes {
    public static final ErrorCode DEFAULT;
    public static final ErrorCode ENTITY_EVENT_HANDLER;
    private ErrorCodes() {
        throw new IllegalArgumentException("No ErrorCodes instance for you");
    }

    public static int readId(ErrorCode code) {
        return code.getId();
    }

    public static String readMessage(ErrorCode code) {
        return code.getMessage();
    }

    static {
        DEFAULT = ErrorCode.create(0, "DEFAULT");
        ENTITY_EVENT_HANDLER = ErrorCode.create(1, "EEH9");
    }
}
