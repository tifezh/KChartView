package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.RSIImpl;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * RSI实现类
 * Created by tifezh on 2016/6/19.
 */

public class RSIDraw implements IChartDraw<RSIImpl> {

    private Paint mRSI1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RSIDraw(Context context) {
        float lineWidth = context.getResources().getDimension(R.dimen.chart_line_width);
        float textSize = context.getResources().getDimension(R.dimen.chart_text_size);

        mRSI1Paint.setColor(ContextCompat.getColor(context,R.color.chart_ma5));
        mRSI1Paint.setStrokeWidth(lineWidth);
        mRSI1Paint.setTextSize(textSize);

        mRSI2Paint.setColor(ContextCompat.getColor(context,R.color.chart_ma10));
        mRSI2Paint.setStrokeWidth(lineWidth);
        mRSI2Paint.setTextSize(textSize);

        mRSI3Paint.setColor(ContextCompat.getColor(context,R.color.chart_ma20));
        mRSI3Paint.setStrokeWidth(lineWidth);
        mRSI3Paint.setTextSize(textSize);
    }

    @Override
    public void drawTranslated(@Nullable RSIImpl lastPoint, @NonNull RSIImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, mRSI1Paint, lastX, lastPoint.getRsi1(), curX, curPoint.getRsi1());
        view.drawChildLine(canvas, mRSI2Paint, lastX, lastPoint.getRsi2(), curX, curPoint.getRsi2());
        view.drawChildLine(canvas, mRSI3Paint, lastX, lastPoint.getRsi3(), curX, curPoint.getRsi3());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        RSIImpl point = (RSIImpl) view.getItem(position);
        text = "RSI1:" + view.formatValue(point.getRsi1()) + " ";
        canvas.drawText(text, x, y, mRSI1Paint);
        x += mRSI1Paint.measureText(text);
        text = "RSI2:" + view.formatValue(point.getRsi2()) + " ";
        canvas.drawText(text, x, y, mRSI2Paint);
        x += mRSI2Paint.measureText(text);
        text = "RSI3:" + view.formatValue(point.getRsi3()) + " ";
        canvas.drawText(text, x, y, mRSI3Paint);
    }

    @Override
    public float getMaxValue(RSIImpl point) {
        return Math.max(point.getRsi1(), Math.max(point.getRsi2(), point.getRsi3()));
    }

    @Override
    public float getMinValue(RSIImpl point) {
        return Math.min(point.getRsi1(), Math.min(point.getRsi2(), point.getRsi3()));
    }

    public void setRSI1Color(int color) {
        mRSI1Paint.setColor(color);
    }

    public void setRSI2Color(int color) {
        mRSI2Paint.setColor(color);
    }

    public void setRSI3Color(int color) {
        mRSI3Paint.setColor(color);
    }
}
