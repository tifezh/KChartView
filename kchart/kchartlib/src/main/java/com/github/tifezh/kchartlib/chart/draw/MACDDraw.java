package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.EntityImpl.MACDImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * macd实现类
 * Created by tifezh on 2016/6/19.
 */

public class MACDDraw extends BaseDraw<MACDImpl> {

    public MACDDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable MACDImpl lastPoint, @NonNull MACDImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMacd());
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        MACDImpl point = (MACDImpl) view.getItem(position);
        text = "DIF:" + view.formatValue(point.getDif()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma5Paint.measureText(text);
        text = "DEA:" + view.formatValue(point.getDea()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma10Paint.measureText(text);
        text = "MACD:" + view.formatValue(point.getMacd()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
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
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, IKChartView view, float x, float macd) {
        macd = view.getChildY(macd);
        int r = mCandleWidth / 2;
        if (macd > view.getChildY(0)) {
            canvas.drawRect(x - r, view.getChildY(0), x + r, macd, greenPaint);
        } else {
            canvas.drawRect(x - r, macd, x + r, view.getChildY(0), redPaint);
        }
    }
}
