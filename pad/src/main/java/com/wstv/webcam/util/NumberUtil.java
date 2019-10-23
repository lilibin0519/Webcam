package com.wstv.webcam.util;

import java.math.BigDecimal;

/**
 * Created by Kindred on 2019/5/12.
 */

public class NumberUtil {

    public static String format10_000(int praise) {
        String result;
        if (praise > 9999) {
            BigDecimal bg = new BigDecimal( praise / (double)10000);
            result = String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "万";
        } else {
            result = String.valueOf(praise);
        }
        return result;
    }
}
