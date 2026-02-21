
package com.bilibili.player_ix.noixmod_api.util;

import java.time.MonthDay;
import java.util.Calendar;

public class TimeSelector {
    public static final int SEC_1D4;
    public static final int HALF_SEC;
    public static final int SEC_Q;
    public static final int ONE_SEC;
    public TimeSelector() {
    }

    public static boolean birthday() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1 == 12
                && MonthDay.now().getDayOfMonth() == 31;
    }

    static {
        SEC_1D4 = 5;
        HALF_SEC = 10;
        SEC_Q = 15;
        ONE_SEC = 20;
    }
}
