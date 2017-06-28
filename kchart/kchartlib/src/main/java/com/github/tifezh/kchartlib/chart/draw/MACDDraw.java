package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.MACDImpl;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * macd实现类
 * Created by tifezh on 2016/6/19.
 */

public class MACDDraw implements IChartDraw<MACDImpl> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mMACDWidth = 0;

    public MACDDraw(Context context) {
        mMACDWidth = context.getResources().getDimension(R.dimen.chart_candle_width);
        float lineWidth = context.getResources().getDimension(R.dimen.chart_line_width);
        float textSize = context.getResources().getDimension(R.dimen.chart_text_size);
        mRedPaint.setColor(ContextCompat.getColor(context,R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context,R.color.chart_green));

        mDIFPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma5));
        mDIFPaint.setStrokeWidth(lineWidth);
        mDIFPaint.setTextSize(textSize);

        mDEAPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma10));
        mDEAPaint.setStrokeWidth(lineWidth);
        mDEAPaint.setTextSize(textSize);

        mMACDPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma20));
        mMACDPaint.setStrokeWidth(lineWidth);
        mMACDPaint.setTextSize(textSize);
    }

    @Override
    public void drawTranslated(@Nullable MACDImpl lastPoint, @NonNull MACDImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMacd());
        view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
        view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        MACDImpl point = (MACDImpl) view.getItem(position);
        text = "DIF:" + view.formatValue(point.getDif()) + " ";
        canvas.drawText(text, x, y, mDEAPaint);
        x += mDIFPaint.measureText(text);
        text = "DEA:" + view.formatValue(point.getDea()) + " ";
        canvas.drawText(text, x, y, mDIFPaint);
        x += mDEAPaint.measureText(text);
        text = "MACD:" + view.formatValue(point.getMacd()) + " ";
        canvas.drawText(text, x, y, mMACDPaint);
    }

    @Override
    public float getMaxValue(MACDImpl point) {
        return Math.max(point.getMacd(), Math.max(point.getDea(), point.getDif()));
    }

    @Override
    public float getMinValue(MACDImpl point) {
        return Math.min(point.getMacd(), Math.min(point.getDea(), point.getDif()));
    }

    /**
     * 画macd
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, IKChartView view, float x, float macd) {
        macd = view.getChildY(macd);
        float r = mMACDWidth / 2;
        if (macd > view.getChildY(0)) {
            canvas.drawRect(x - r, view.getChildY(0), x + r, macd, mRedPaint);
        } else {
            canvas.drawRect(x - r, macd, x + r, view.getChildY(0), mGreenPaint);
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
}
