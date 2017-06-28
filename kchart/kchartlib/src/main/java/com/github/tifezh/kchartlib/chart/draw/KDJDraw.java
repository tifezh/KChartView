package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.KDJImpl;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * KDJ实现类
 * Created by tifezh on 2016/6/19.
 */

public class KDJDraw implements IChartDraw<KDJImpl> {

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Context mContext;

    public KDJDraw(Context context) {
        mContext=context;
        float lineWidth = context.getResources().getDimension(R.dimen.chart_line_width);
        float textSize = context.getResources().getDimension(R.dimen.chart_text_size);

        mKPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma5));
        mKPaint.setStrokeWidth(lineWidth);
        mKPaint.setTextSize(textSize);

        mDPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma10));
        mDPaint.setStrokeWidth(lineWidth);
        mDPaint.setTextSize(textSize);

        mJPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma20));
        mJPaint.setStrokeWidth(lineWidth);
        mJPaint.setTextSize(textSize);
    }

    @Override
    public void drawTranslated(@Nullable KDJImpl lastPoint, @NonNull KDJImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        KDJImpl point = (KDJImpl) view.getItem(position);
        text = "K:" + view.formatValue(point.getK()) + " ";
        canvas.drawText(text, x, y, mKPaint);
        x += mKPaint.measureText(text);
        text = "D:" + view.formatValue(point.getD()) + " ";
        canvas.drawText(text, x, y, mDPaint);
        x += mDPaint.measureText(text);
        text = "J:" + view.formatValue(point.getJ()) + " ";
        canvas.drawText(text, x, y, mJPaint);
    }

    @Override
    public float getMaxValue(KDJImpl point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(KDJImpl point) {
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
}
