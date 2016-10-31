package com.github.tifezh.kchart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.chart.BaseKChart;
import com.github.tifezh.kchartlib.chart.draw.BOLLDraw;
import com.github.tifezh.kchartlib.chart.draw.KDJDraw;
import com.github.tifezh.kchartlib.chart.draw.MACDDraw;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.RSIDraw;

/**
 * k线图
 * Created by tian on 2016/5/20.
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
}
