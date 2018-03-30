package com.github.tifezh.kchartlib.chart;

import android.graphics.Rect;
import android.support.annotation.CallSuper;
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
    
    private float mScaleY;

    public BaseChartDraw(@NonNull Rect rect) {
        mRect = rect;
    }

    @SuppressWarnings("unchecked")
    @Override
    @CallSuper
    public void calculate(@NonNull View view) {
        BaseKChartView kChartView = (BaseKChartView) view;
        mMaxValue = Float.MIN_VALUE;
        mMinValue = Float.MAX_VALUE;
        int start = kChartView.getStartIndex();
        int end = kChartView.getStopIndex();
        for (int i = start; i < end; i++) {
            T point = (T) kChartView.getItem(i);
            foreach (view,i,point);
            mMaxValue = Math.max(mMaxValue, getMaxValue(point));
            mMinValue = Math.min(mMinValue, getMinValue(point));
        }
        if(mMaxValue!=mMinValue) {
            float padding = (mMaxValue - mMinValue) * 0.05f;
            mMaxValue += padding;
            mMinValue -= padding;
        } else {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mMaxValue += Math.abs(mMaxValue*0.05f);
            mMinValue -= Math.abs(mMinValue*0.05f);
            if (mMaxValue == 0) {
                mMaxValue = 1;
            }
        }
        mScaleY = mRect.height() * 1f / (mMaxValue - mMinValue);
    }

    /**
     * 循环遍历显示区域的实体
     * 在{@link BaseChartDraw#calculate(View)} 中被循环调用
     */
    protected void foreach(View view,int i,T point){

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
    abstract protected float getMaxValue(T point);

    /**
     * 获取当前实体中最小的值
     *
     * @param point 实体
     */
    abstract protected float getMinValue(T point);

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

    public float getY(float value) {
        return (mMaxValue - value) * mScaleY;
    }
}
