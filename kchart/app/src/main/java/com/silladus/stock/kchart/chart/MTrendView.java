package com.silladus.stock.kchart.chart;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.tifezh.kchartlib.chart.BaseKChart;
import com.github.tifezh.kchartlib.chart.draw.MINDraw;
import com.github.tifezh.kchartlib.chart.draw.VOLDraw;

import static com.github.tifezh.kchartlib.utils.ViewUtil.dp2Px;

/**
 * 分时
 * Created by silladus on 2017/3/7.
 */

public class MTrendView extends BaseKChart {
    public static final int PADDING_VALUE = 5;
    public MTrendView(Context context) {
        this(context, null);
    }

    public MTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTopPadding = dp2Px(context, PADDING_VALUE);
        addChildDraw("VOL", new VOLDraw(getContext()));
        setMainDraw(new MINDraw(getContext()));
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Activity activity = (Activity) getContext();
        boolean isVertical = (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        if (isVertical) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        return true;
    }
}
