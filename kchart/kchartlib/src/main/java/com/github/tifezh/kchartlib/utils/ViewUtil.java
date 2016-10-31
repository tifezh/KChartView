package com.github.tifezh.kchartlib.utils;

import android.content.Context;

/**
 * Created by tian on 2016/4/11.
 */
public class ViewUtil {
    static public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    static public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
