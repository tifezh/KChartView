package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.EntityImpl.CandleImpl;
import com.github.tifezh.kchartlib.chart.EntityImpl.KLineImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * 主图的实现类
 * Created by tifezh on 2016/6/14.
 */

public class MainDraw extends BaseDraw<CandleImpl> {

    /**
     * 构造方法
     *
     * @param context
     */
    public MainDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable CandleImpl lastPoint, @NonNull CandleImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
        //画ma5
        if (lastPoint.getMA5Price() != 0) {
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
        }
        //画ma10
        if (lastPoint.getMA10Price() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
        }
        //画ma20
        if (lastPoint.getMA20Price() != 0) {
            view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX, curPoint.getMA20Price());
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        CandleImpl point = (KLineImpl) view.getItem(position);
        String text = "MA5:" + view.formatValue(point.getMA5Price()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Price()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "MA20:" + view.formatValue(point.getMA20Price()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
    }

    @Override
    public float getMaxValue(CandleImpl point) {
        return Math.max(point.getHighPrice(), point.getMA20Price());
    }

    @Override
    public float getMinValue(CandleImpl point) {

        float min = Float.MAX_VALUE;
        min = Math.min(min, point.getLowPrice());
        //防止没有ma20的点计入
        if (point.getMA20Price() != 0) {
            min = Math.min(min, point.getMA20Price());
        }
        return min;
    }

    /**
     * 画Candle
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(IKChartView view, Canvas canvas, float x, float high, float low, float open, float close) {
        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        int r = mCandleWidth / 2;
        int lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            canvas.drawRect(x - r, close, x + r, open, redPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, redPaint);
        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, greenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, greenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, redPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, redPaint);
        }
    }
}
