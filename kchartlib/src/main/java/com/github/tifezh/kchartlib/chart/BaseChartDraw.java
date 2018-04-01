package com.github.tifezh.kchartlib.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;

import java.util.List;

/**
 * 图形绘制基类
 * Created by tifezh on 2018/3/30.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseChartDraw<T> implements IChartDraw<T> {

    private float mMaxValue;

    private float mMinValue;

    private Rect mRect;
    
    private float mScaleY;

    protected BaseKChartView mKChartView;

    /**
     * 构造方法
     * @param rect 显示区域
     * @param KChartView {@link BaseKChartView}
     */
    public BaseChartDraw(Rect rect, BaseKChartView KChartView) {
        mRect = rect;
        mKChartView = KChartView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mKChartView.translateX(), getTop());
        canvas.scale(mKChartView.getScaleX(), 1);
        drawCharts(canvas,mKChartView.getStartIndex(),mKChartView.getStopIndex());
        canvas.restore();
        canvas.save();
        canvas.translate(0,getTop());
        drawValues(canvas,mKChartView.getStartIndex(),mKChartView.getStopIndex());
        canvas.restore();
    }

    @Override
    @CallSuper
    public void calculate(int start,int stop) {
        mMaxValue = Float.MIN_VALUE;
        mMinValue = Float.MAX_VALUE;
        List<T> data = getData();
        for (int i = start; i < stop; i++) {
            T point = data.get(i);
            foreachCalculate (i,point);
        }
//        if(mMaxValue!=mMinValue) {
//            float padding = (mMaxValue - mMinValue) * 0.05f;
//            mMaxValue += padding;
//            mMinValue -= padding;
//        } else {
//            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
//            mMaxValue += 1f;
//            mMinValue -= 1f;
//            if (mMaxValue == 0) {
//                mMaxValue = 1;
//            }
//        }

        mScaleY = mRect.height() * 1f / (mMaxValue - mMinValue);
    }

    @Override
    @CallSuper
    public void drawCharts(@NonNull Canvas canvas, int start, int stop) {
        List<T> data = getData();
        for (int i = mKChartView.getStartIndex(); i <= mKChartView.getStopIndex(); i++) {
            T currentPoint = data.get(i);
            T lastPoint = i == mKChartView.getStartIndex() ? currentPoint : data.get(i - 1);
            foreachDrawChart(canvas,i,currentPoint,lastPoint);
        }
    }

    /**
     * 循环遍历显示区域的实体
     * 在{@link BaseChartDraw#calculate(List, int, int)}} 中被循环调用
     * @param i 索引
     * @param point 数据实体
     */
    @CallSuper
    protected void foreachCalculate(int i,T point){
        mMaxValue = Math.max(mMaxValue, getMaxValue(point));
        mMinValue = Math.min(mMinValue, getMinValue(point));
    }

    /**
     * 循环遍历显示区域的实体
     * 在{@link BaseChartDraw#drawCharts(Canvas, int, int)} 中被调用
     * @param i 当前点的索引值
     * @param curPoint 当前点实体
     * @param lastPoint 上一个点实体
     */
    abstract protected void foreachDrawChart(Canvas canvas,int i,T curPoint,T lastPoint);

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

    @Override
    public int getHeight() {
        return mRect.height();
    }

    @Override
    public int getWidth() {
        return mRect.width();
    }

    @Override
    public float getY(float value) {
        return   (mMaxValue - value) * mScaleY;
    }

    /**
     * 获取全部数据集合
     */
    @SuppressWarnings("unchecked")
    protected List<T> getData(){
        return mKChartView.getAdapter().getData();
    }

    /**
     * 获取实体个数
     */
    protected int getDataCount(){
        return mKChartView.getAdapter().getCount();
    }

    /**
     * 根据索引索取x坐标
     * @param index 索引值
     * @see BaseKChartView#getX(int)
     */
    public float getX(int index) {
        return mKChartView.getX(index);
    }

    /**
     * 画线
     * @param curIndex 当前点索引
     * @param curValue 当前点的值
     * @param lastValue 前一个点的值
     */
    public void drawLine(Canvas canvas, Paint paint,int curIndex, float curValue, float lastValue){
        //如果是第一个点就不用画线
        if(curIndex!=mKChartView.getStartIndex()){
            canvas.drawLine(getX(curIndex-1),getY(lastValue),getX(curIndex),getY(curValue),paint);
        }
    }

    /**
     * 画矩形
     * @param curIndex 当前点的index
     * @param width 矩形的宽度
     * @param topValue 上方的值
     * @param bottomValue 底部的值
     */
    public void drawRect(Canvas canvas,Paint paint,int curIndex,float width,float topValue,float bottomValue){
        topValue=getY(topValue);
        bottomValue=getY(bottomValue);
        float x = getX(curIndex);
        canvas.drawRect(x-width/2f,topValue,x+width/2f,bottomValue,paint);
    }

    /**
     * 获取显示的值
     * 长按状态下显示长按的值
     * 非长按状态下显示最右边的值
     */
    @SuppressWarnings("unchecked")
    public T getDisplayItem(){
        return (T) mKChartView.getItem(mKChartView.isLongPress()?mKChartView.getSelectedIndex():mKChartView.getStopIndex());
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return mKChartView.getValueFormatter();
    }
}
