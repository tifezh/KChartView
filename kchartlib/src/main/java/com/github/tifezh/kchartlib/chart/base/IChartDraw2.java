package com.github.tifezh.kchartlib.chart.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.View;


/**
 * 画图的基类 根据实体来画图形
 * @param <T> 实体类泛型
 * Created by tifezh on 2018/3/29.
 */

public interface IChartDraw2<T> {

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
     * 获取绘制区域的地步
     */
    int getBottom();

    /**
     * 在绘制之前计算
     * @param view 图形绘制所处的View
     */
    void calculate(@NonNull View view);

    /**
     * 画图表
     * 注意：在此方法画出来的图表会缩放和平移
     * @param view 图形绘制所处的View
     * @param canvas {@link Canvas}
     */
    void drawCharts(@NonNull View view,@NonNull Canvas canvas);

    /**
     * 画数值
     * 注意：在此方法画出来的图表不会缩放和平移
     * @param view 图形绘制所处的View
     * @param canvas {@link Canvas}
     */
    void drawValues(@NonNull View view,@NonNull Canvas canvas);

    /**
     * 获取当前显示区域实体中最大的值
     */
    float getMaxValue();

    /**
     * 获取当前显示区域实体中最小的值
     */
    float getMinValue();
}
