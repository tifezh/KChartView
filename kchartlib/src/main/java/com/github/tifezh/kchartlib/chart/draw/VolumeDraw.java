package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.BaseChartDraw;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.entity.IVolume;
import com.github.tifezh.kchartlib.chart.formatter.BigValueFormatter;
import com.github.tifezh.kchartlib.utils.CanvasUtils;
import com.github.tifezh.kchartlib.utils.ViewUtil;

/**
 * 成交量
 * Created by hjm on 2017/11/14 17:49.
 */

public class VolumeDraw extends BaseChartDraw<IVolume> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int pillarWidth = 0;

    /**
     * 构造方法
     *
     * @param rect       显示区域
     * @param KChartView {@link BaseKChartView}
     */
    public VolumeDraw(Rect rect, BaseKChartView KChartView) {
        super(rect, KChartView);
        Context context = mKChartView.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
        pillarWidth = ViewUtil.Dp2Px(context, 4);
    }


    private void drawHistogram(
            Canvas canvas, IVolume curPoint,float curX) {

        float r = pillarWidth / 2;
        float top = getY(curPoint.getVolume());
        float bottom = getY(0);
        if (curPoint.getClosePrice() >= curPoint.getOpenPrice()) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }

    }

    @Override
    public void drawValues(@NonNull Canvas canvas, int start, int stop) {
        IVolume point = getDisplayItem();
        float x = mKChartView.getTextPaint().measureText(getValueFormatter().format(getMaxValue())+" ");
        CanvasUtils.drawTexts(canvas,x,0, CanvasUtils.XAlign.LEFT, CanvasUtils.YAlign.TOP,
                new Pair<>(mKChartView.getTextPaint(),"VOL:" + getValueFormatter().format(point.getVolume()) + " "),
                new Pair<>(ma5Paint,"MA5:" + getValueFormatter().format(point.getMA5Volume()) + " "),
                new Pair<>(ma10Paint,"MA10:" + getValueFormatter().format(point.getMA10Volume()) + " "));
    }

    @Override
    protected void foreachDrawChart(Canvas canvas, int i, IVolume curPoint, IVolume lastPoint) {
        drawHistogram(canvas, curPoint, getX(i));
        drawLine(canvas, ma5Paint, i, curPoint.getMA5Volume(), lastPoint.getMA5Volume());
        drawLine(canvas, ma10Paint, i, curPoint.getMA10Volume(), lastPoint.getMA10Volume());
    }

    @Override
    public float getMaxValue(IVolume point) {
        return Math.max(point.getVolume(), Math.max(point.getMA5Volume(), point.getMA10Volume()));
    }

    @Override
    public float getMinValue(IVolume point) {
        return 0;
    }

    public IValueFormatter getValueFormatter() {
        return new BigValueFormatter();
    }

    /**
     * 设置 MA5 线的颜色
     *
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置 MA10 线的颜色
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     *
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
    }


}
