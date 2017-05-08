package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.VOLImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * VOL实现类
 * Created by silladus on 2017/3/6.
 */

public class VOLDraw extends BaseDraw<VOLImpl> {
    private boolean isMainChart;

    public VOLDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable VOLImpl lastPoint, @NonNull VOLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawVOL(canvas, view, lastX, curX, curPoint);
        if (isMainChart) {
            if (position > 4) {
                view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getVMA5(), curX, curPoint.getVMA5());
            }
            if (position > 9) {
                view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getVMA10(), curX, curPoint.getVMA10());
            }
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        VOLImpl point = (VOLImpl) view.getItem(position);
        if (point.getVOL() > 1000000) {
            text = "VOL:" + view.formatValue(point.getVOL() / 1000000f) + "万手 ";
        } else {
            text = "VOL:" + view.formatValue(point.getVOL() / 100f) + "手 ";
        }
        canvas.drawText(text, x, y, ma10Paint);
        if (isMainChart) {
            x += ma5Paint.measureText(text);
            text = "MA5:" + view.formatValue(point.getVMA5() / 1000000f) + "万手 ";
            canvas.drawText(text, x, y, ma5Paint);
            x += ma10Paint.measureText(text);
            text = "MA10:" + view.formatValue(point.getVMA10() / 1000000f) + "万手 ";
            canvas.drawText(text, x, y, ma10Paint);
        }
    }

    @Override
    public float getMaxValue(VOLImpl point) {
        return Math.max(point.getVOL(), Math.max(point.getVMA5(), point.getVMA10()));
    }

    @Override
    public float getMinValue(VOLImpl point) {
        return 0;
    }

    /**
     * 画VOL
     *
     * @param canvas
     * @param lastX
     * @param cutX
     * @param curPoint
     */
    private void drawVOL(Canvas canvas, IKChartView view, float lastX, float cutX, VOLImpl curPoint) {
        float closeDif = curPoint.getCloseDif();
        float vol = view.getChildY(curPoint.getVOL());
        float eX = cutX - lastX;
//        int r = eX < 2 ? 1 : (int) (eX / 2);
        float r = eX / 2;
        if (isMainChart) {
            r = mCandleWidth / 2;
            if (closeDif < 0) {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
            } else if (closeDif == 0) {
                if (curPoint.getRate()) {
                    canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
                } else {
                    canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
                }
            } else {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            }
        } else {
            if (closeDif < 0) {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
            } else if (closeDif == 0) {
                redPaint.setColor(Color.LTGRAY);
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            } else {
                redPaint.setColor(mContext.getResources().getColor(R.color.chart_red));
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            }
        }
    }

    public VOLDraw withMainChart(boolean isMainChart) {
        this.isMainChart = isMainChart;
        return this;
    }
}
