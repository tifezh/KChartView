package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.entity.IMACD;
import com.github.tifezh.kchartlib.utils.CanvasUtils;
import com.github.tifezh.kchartlib.utils.XAlign;
import com.github.tifezh.kchartlib.utils.YAlign;

/**
 * Created by tifezh on 2018/3/30.
 */

@SuppressWarnings("ALL")
public class MACDDraw extends BaseChartDraw<IMACD>{

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**macd 中柱子的宽度*/
    private float mMACDWidth = 10;

    public MACDDraw(Rect rect,BaseKChartView view) {
        super(rect,view);
        Context context=view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context,R.color.chart_green));

        setLineWidth(mKChartView.getLineWidth());
        setTextSize(mKChartView.getTextSize());
        mDEAPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma5));
        mDIFPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma10));
    }

    @Override
    public void drawValues(@NonNull Canvas canvas,int start,int stop) {
        IMACD point = getDisplayItem();
        float x = mKChartView.getTextPaint().measureText(getValueFormatter().format(getMaxValue())+" ");
        CanvasUtils.drawTexts(canvas,x,0, XAlign.LEFT, YAlign.TOP,
                new Pair<>(mDIFPaint,"DIF:" + mKChartView.formatValue(point.getDif()) + " "),
                new Pair<>(mDEAPaint,"DEA:" + mKChartView.formatValue(point.getDea()) + " "),
                new Pair<>(mMACDPaint,"MACD:" + mKChartView.formatValue(point.getMacd()) + " "));
    }

    @Override
    protected void foreachDrawChart(Canvas canvas, int i, IMACD curPoint, IMACD lastPoint) {
        drawMACD(canvas,i, curPoint.getMacd());
        drawLine(canvas,mDEAPaint,i,curPoint.getDea(),lastPoint.getDea());
        drawLine(canvas,mDIFPaint,i,curPoint.getDif(),lastPoint.getDif());
    }

    @Override
    public float getMaxValue(IMACD point) {
        return Math.max(point.getMacd(), Math.max(point.getDea(), point.getDif()));
    }

    @Override
    public float getMinValue(IMACD point) {
        return Math.min(point.getMacd(), Math.min(point.getDea(), point.getDif()));
    }
    /**
     * 画macd
     */
    private void drawMACD(Canvas canvas, int index, float macd) {
        if (macd > 0) {
            drawRect(canvas,mRedPaint,index,mMACDWidth,macd,0);
        } else {
            drawRect(canvas,mGreenPaint,index,mMACDWidth,0,macd);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.mDEAPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.mMACDPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDWidth = MACDWidth;
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width)
    {
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize)
    {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
    }
}
