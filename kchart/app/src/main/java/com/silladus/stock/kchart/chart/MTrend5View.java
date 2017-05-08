package com.silladus.stock.kchart.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.chart.BaseKChart;
import com.github.tifezh.kchartlib.chart.draw.MIN5Draw;
import com.github.tifezh.kchartlib.chart.draw.VOLDraw;

import static com.github.tifezh.kchartlib.utils.ViewUtil.dp2Px;

/**
 * 五日分时
 * Created by silladus on 2017/3/7.
 */

public class MTrend5View extends BaseKChart {
    public static final int PADDING_VALUE = 5;
    public MTrend5View(Context context) {
        this(context, null);
    }

    public MTrend5View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTrend5View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTopPadding = dp2Px(context, PADDING_VALUE);
        addChildDraw("VOL", new VOLDraw(getContext()));
        setMainDraw(new MIN5Draw(getContext()));
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }
}
