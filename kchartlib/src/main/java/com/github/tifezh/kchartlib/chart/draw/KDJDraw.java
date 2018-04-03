package com.github.tifezh.kchartlib.chart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.tifezh.kchartlib.chart.BaseChartDraw;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.entity.IKDJ;
import com.github.tifezh.kchartlib.utils.CanvasUtils;
import com.github.tifezh.kchartlib.utils.XAlign;
import com.github.tifezh.kchartlib.utils.YAlign;

/**
 * KDJ实现类
 * Created by tifezh on 2016/6/19.
 */

public class KDJDraw extends BaseChartDraw<IKDJ> {

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 构造方法
     *
     * @param rect       显示区域
     * @param KChartView {@link BaseKChartView}
     */
    public KDJDraw(Rect rect, BaseKChartView KChartView) {
        super(rect, KChartView);
    }


    @Override
    protected void foreachDrawChart(Canvas canvas, int i, IKDJ curPoint, IKDJ lastPoint) {
        drawLine(canvas, mKPaint, i, curPoint.getK(), lastPoint.getK());
        drawLine(canvas, mDPaint, i, curPoint.getD(), lastPoint.getD());
        drawLine(canvas, mJPaint, i, curPoint.getJ(), lastPoint.getJ());
    }

    @Override
    public void drawValues(@NonNull Canvas canvas, int start, int stop) {
        IKDJ point = getDisplayItem();
        float x = mKChartView.getTextPaint().measureText(getValueFormatter().format(getMaxValue())+" ");
        CanvasUtils.drawTexts(canvas,x,0, XAlign.LEFT, YAlign.TOP,
                new Pair<>(mKPaint,"K:" + mKChartView.formatValue(point.getK()) + " "),
                new Pair<>(mDPaint,"D:" + mKChartView.formatValue(point.getD()) + " "),
                new Pair<>(mJPaint,"J:" + mKChartView.formatValue(point.getJ()) + " "));
    }

    @Override
    public float getMaxValue(IKDJ point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(IKDJ point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width)
    {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize)
    {
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
    }


}
