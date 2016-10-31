package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Paint;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.utils.ViewUtil;

/**
 * draw基类 创建画笔等
 * Created by tifezh on 2016/6/19.
 */

public abstract class BaseDraw<T> implements IChartDraw<T> {

    protected float mTextSize = 0;
    //candle的宽度
    protected int mCandleWidth = 0;
    //candle线的宽度
    protected int mCandleLineWidth = 0;
    //线的宽度
    protected int mLineWidth = 0;
    protected Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected Context mContext;

    public BaseDraw(Context context) {
        mCandleWidth = ViewUtil.Dp2Px(context, 4);
        mCandleLineWidth = ViewUtil.Dp2Px(context, 1);
        mLineWidth = ViewUtil.Dp2Px(context, 0.5f);
        mTextSize = context.getResources().getDimension(R.dimen.chart_text_size);
        redPaint.setColor(context.getResources().getColor(R.color.chart_red));
        greenPaint.setColor(context.getResources().getColor(R.color.chart_green));

        ma5Paint.setColor(context.getResources().getColor(R.color.chart_ma5));
        ma5Paint.setStrokeWidth(mLineWidth);
        ma5Paint.setTextSize(mTextSize);

        ma10Paint.setColor(context.getResources().getColor(R.color.chart_ma10));
        ma10Paint.setStrokeWidth(mLineWidth);
        ma10Paint.setTextSize(mTextSize);

        ma20Paint.setColor(context.getResources().getColor(R.color.chart_ma20));
        ma20Paint.setStrokeWidth(mLineWidth);
        ma20Paint.setTextSize(mTextSize);

        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String formatValue(float value) {
        return String.valueOf(value);
    }
}
