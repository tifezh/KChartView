package com.silladus.stock.kchart.chart;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.tifezh.kchartlib.chart.BaseKChart;
import com.github.tifezh.kchartlib.chart.draw.BOLLDraw;
import com.github.tifezh.kchartlib.chart.draw.KDJDraw;
import com.github.tifezh.kchartlib.chart.draw.MACDDraw;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.RSIDraw;
import com.github.tifezh.kchartlib.chart.draw.VOLDraw;

/**
 * k线图
 * Created by silladus on 2017/3/6.
 */
public class KChartView extends BaseKChart {

    public KChartView(Context context) {
        this(context, null);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addChildDraw("VOL", new VOLDraw(getContext()).withMainChart(true));
        addChildDraw("MACD", new MACDDraw(getContext()));
        addChildDraw("KDJ", new KDJDraw(getContext()));
        addChildDraw("RSI", new RSIDraw(getContext()));
        addChildDraw("BOLL", new BOLLDraw(getContext()));
        setMainDraw(new MainDraw(getContext()));
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
