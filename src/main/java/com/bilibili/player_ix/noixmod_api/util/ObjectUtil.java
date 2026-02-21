
package com.bilibili.player_ix.noixmod_api.util;

/**A util about objects
 * @see Object
 * @author Player_IX*/
public final class ObjectUtil {
    public static final Boolean FALSE = Boolean.FALSE;
    public static final Boolean TRUE = Boolean.TRUE;
    public ObjectUtil() {
    }

    public static boolean nonnullEquals(Object a, Object b) {
        return a != null && a.equals(b);
    }

    public static class UnsupportedTypeException
    extends IllegalArgumentException {
        private final String string;
        public UnsupportedTypeException(String more) {
            super(more);
            this.string = more;
        }

        public String getMessage() {
            return "UnsupportedType for" + this.string;
        }

        public synchronized Throwable getCause() {
            return this;
        }

        public synchronized Throwable initCause(Throwable cause) {
            return this;
        }
    }
}
