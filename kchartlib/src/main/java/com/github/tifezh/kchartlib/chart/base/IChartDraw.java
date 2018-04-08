package com.github.tifezh.kchartlib.chart.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

/**
 * 画图的基类 根据实体来画图形
 * @param <T> 实体类泛型
 * Created by tifezh on 2018/3/29.
 */

@SuppressWarnings("unused")
public interface IChartDraw<T> extends IDraw{

    /**
     * 获取绘制区域的顶部
     */
    int getTop();

    /**
     * 获取绘制区域的左部
     */
    int getLeft();

    /**
     * 获取绘制区域的右部
     */
    int getRight();

    /**
     * 获取绘制区域的底部
     */
    int getBottom();

    /**
     * 获取绘制高度
     */
    int getHeight();

    /**
     * 获取绘制宽度
     */
    int getWidth();

    /**
     * 在绘制之前计算
     * @param start 显示区域实体索引的开始
     * @param stop 显示区域实体索引的结束
     */
    void calculate(int start, int stop);

    /**
     * 画图表
     * 注意：在此方法画出来的图表会缩放和平移
     * @param canvas {@link Canvas}
     * @param start 显示区域实体索引的开始
     * @param stop 显示区域实体索引的结束
     */
    void drawCharts(@NonNull Canvas canvas, int start, int stop);

    /**
     * 画数值
     * 注意：在此方法画出来的图表不会缩放和平移
     * @param canvas {@link Canvas}
     * @param start 显示区域实体索引的开始
     * @param stop 显示区域实体索引的结束
     */
    void drawValues(@NonNull Canvas canvas,int start,int stop);

    /**
     * 获取当前显示区域实体中最大的值
     */
    float getMaxValue();

    /**
     * 获取当前显示区域实体中最小的值
     */
    float getMinValue();

    /**
     * 将数值转换为坐标值
     */
    float getY(float value);

    /**
     * 获取格式化器
     */
    IValueFormatter getValueFormatter();
}