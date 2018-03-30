package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.BaseChartDraw;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.entity.IMACD;

/**
 * Created by tifezh on 2018/3/30.
 */

@SuppressWarnings("ALL")
public class MACDDraw2 extends BaseChartDraw<IMACD>{

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**macd 中柱子的宽度*/
    private float mMACDWidth = 0;

    private Path dea;
    private Path dif;

    private BaseKChartView mKChartView;

    public MACDDraw2(BaseKChartView view,Rect rect) {
        super(rect);
        Context context=view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context,R.color.chart_green));
        mKChartView = view;
    }

    @Override
    public void calculate(@NonNull View view) {
        super.calculate(view);
        dea=new Path();
        dif=new Path();
    }

    @Override
    protected void foreach(View view,int i,IMACD point) {
        super.foreach(view,i,point);
        dea.lineTo(mKChartView.getX(i),getY(point.getDea()));
        dif.lineTo(mKChartView.getX(i),getY(point.getDif()));
    }

    @Override
    public void drawCharts(@NonNull View view, @NonNull Canvas canvas) {
//        drawMACD(canvas, view, curX, curPoint.getMacd());
        canvas.drawPath(dea,mDEAPaint);
        canvas.drawPath(dif,mDIFPaint);
    }

    @Override
    public void drawValues(@NonNull View view, @NonNull Canvas canvas) {
        String text = "";
        int x=0;
        int y=0;
        IMACD point = (IMACD) mKChartView.getItem(mKChartView.isLongPress()?mKChartView.getSelectedIndex():mKChartView.getStopIndex());
        text = "DIF:" + mKChartView.formatValue(point.getDif()) + " ";
        canvas.drawText(text, x, y, mDEAPaint);
        x += mDIFPaint.measureText(text);
        text = "DEA:" + mKChartView.formatValue(point.getDea()) + " ";
        canvas.drawText(text, x, y, mDIFPaint);
        x += mDEAPaint.measureText(text);
        text = "MACD:" + mKChartView.formatValue(point.getMacd()) + " ";
        canvas.drawText(text, x, y, mMACDPaint);
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
    private void drawMACD(Canvas canvas, BaseKChartView view, float x, float macd) {
        float macdy = view.getChildY(macd);
        float r = mMACDWidth / 2;
        float zeroy = view.getChildY(0);
        if (macd > 0) {
            //               left   top   right  bottom
            canvas.drawRect(x - r, macdy, x + r, zeroy, mRedPaint);
        } else {
            canvas.drawRect(x - r, zeroy, x + r, macdy, mGreenPaint);
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
