package com.github.tifezh.kchartlib.chart;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.tifezh.kchartlib.chart.base.IChartDraw2;

/**
 * 图形绘制基类
 * Created by tifezh on 2018/3/30.
 */

public abstract class BaseChartDraw<T> implements IChartDraw2<T> {

    private float mMaxValue;

    private float mMinValue;

    private Rect mRect;

    public BaseChartDraw(@NonNull Rect rect) {
        mRect = rect;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void calculate(@NonNull View view) {
        BaseKChartView kChartView = (BaseKChartView) view;
        mMaxValue = Float.MIN_VALUE;
        mMinValue = Float.MAX_VALUE;
        int start = kChartView.getStartIndex();
        int end = kChartView.getStopIndex();
        for (int i = start; i < end; i++) {
            T point = (T) kChartView.getItem(i);
            mMaxValue = Math.max(mMaxValue, getMaxValue(point));
            mMinValue = Math.min(mMinValue, getMinValue(point));
        }
    }

    @Override
    public float getMaxValue() {
        return mMaxValue;
    }

    @Override
    public float getMinValue() {
        return mMinValue;
    }

    /**
     * 获取当前实体中最大的值
     *
     * @param point 当前实体
     */
    abstract float getMaxValue(T point);

    /**
     * 获取当前实体中最小的值
     *
     * @param point 实体
     */
    abstract float getMinValue(T point);

    @Override
    public int getTop() {
        return mRect.top;
    }

    @Override
    public int getBottom() {
        return mRect.bottom;
    }

    @Override
    public int getLeft() {
        return mRect.left;
    }

    @Override
    public int getRight() {
        return mRect.right;
    }
}
