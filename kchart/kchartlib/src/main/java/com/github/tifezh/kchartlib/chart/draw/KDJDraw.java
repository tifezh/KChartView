package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.EntityImpl.KDJImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * KDJ实现类
 * Created by tifezh on 2016/6/19.
 */

public class KDJDraw extends BaseDraw<KDJImpl> {

    public KDJDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable KDJImpl lastPoint, @NonNull KDJImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, ma20Paint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        KDJImpl point = (KDJImpl) view.getItem(position);
        text = "K:" + view.formatValue(point.getK()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "D:" + view.formatValue(point.getD()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "J:" + view.formatValue(point.getJ()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
    }

    @Override
    public float getMaxValue(KDJImpl point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(KDJImpl point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

}
