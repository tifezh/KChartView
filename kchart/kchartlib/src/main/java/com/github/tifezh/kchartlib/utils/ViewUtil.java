package com.github.tifezh.kchartlib.utils;

import android.content.Context;

/**
 * Created by silladus on 2017/3/10.
 */
public class ViewUtil {
    static public int dp2Px(Context context, float dp) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dp * scale + 0.5f);
        return Math.round(context.getResources().getDisplayMetrics().density * dp);
    }

    static public int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
