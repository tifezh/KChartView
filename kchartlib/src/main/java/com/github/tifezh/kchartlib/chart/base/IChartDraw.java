package com.github.tifezh.kchartlib.chart.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.BaseKChartView;


/**
 * 画图的基类 根据实体来画图形
 * Created by tifezh on 2016/6/14.
 */

public interface IChartDraw<T> {
    /**
     * 需要滑动 物体draw方法
     *
     * @param canvas    canvas
     * @param view      k线图View
     * @param position  当前点的位置
     * @param lastPoint 上一个点
     * @param curPoint  当前点
     * @param lastX     上一个点的x坐标
     * @param curX      当前点的X坐标
     */
    void drawTranslated(@Nullable T lastPoint, @NonNull T curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position);

    /**
     * @param canvas
     * @param view
     * @param position 该点的位置
     * @param x        x的起始坐标
     * @param y        y的起始坐标
     */
    void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y);

    /**
     * 获取当前实体中最大的值
     *
     * @param point
     * @return
     */
    float getMaxValue(T point);

    /**
     * 获取当前实体中最小的值
     *
     * @param point
     * @return
     */
    float getMinValue(T point);

    /**
     * 获取value格式化器
     */
    IValueFormatter getValueFormatter();
}
