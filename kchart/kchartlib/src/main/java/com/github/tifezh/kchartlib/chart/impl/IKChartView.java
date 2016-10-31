package com.github.tifezh.kchartlib.chart.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * KChartView 的抽象类
 * Created by tifezh on 2016/6/14.
 */

public interface IKChartView {
    /**
     * 设置数据适配器
     *
     * @param adapter IAdapter
     */
    void setAdapter(IAdapter adapter);

    /**
     * 给子区域添加画图方法
     *
     * @param name 显示的文字标签
     * @param draw IChartDraw
     */
    void addChildDraw(String name, @NonNull IChartDraw draw);

    /**
     * 将实际值转换为主图的坐标值
     *
     * @param value
     */
    float getMainY(float value);

    /**
     * 将实际值转换为子图的坐标值
     *
     * @param value
     * @return
     */
    float getChildY(float value);

    /**
     * 在主区域画线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopX     结束点的横坐标
     * @param stopValue 结束点的值
     */
    void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue);

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue);

    /**
     * 通过序号获取item
     *
     * @param position
     * @return
     */
    Object getItem(int position);

    /**
     * 获取当前点的x坐标
     *
     * @param position
     * @return
     */
    float getX(int position);

    /**
     * 获取主图的高度
     *
     * @return
     */
    int getMainHeight();

    /**
     * 获取tabbar的高度
     *
     * @return
     */
    int getTabBarHeight();

    /**
     * 格式化value
     *
     * @param value
     */
    String formatValue(float value);
}
