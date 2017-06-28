package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.BOLLImpl;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * BOLL实现类
 * Created by tifezh on 2016/6/19.
 */

public class BOLLDraw implements IChartDraw<BOLLImpl> {

    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Context mContext;

    public BOLLDraw(Context context) {
        mContext=context;
        float lineWidth = context.getResources().getDimension(R.dimen.chart_line_width);
        float textSize = context.getResources().getDimension(R.dimen.chart_text_size);

        mUpPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma5));
        mUpPaint.setStrokeWidth(lineWidth);
        mUpPaint.setTextSize(textSize);

        mMbPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma10));
        mMbPaint.setStrokeWidth(lineWidth);
        mMbPaint.setTextSize(textSize);

        mDnPaint.setColor(ContextCompat.getColor(context,R.color.chart_ma20));
        mDnPaint.setStrokeWidth(lineWidth);
        mDnPaint.setTextSize(textSize);
    }

    @Override
    public void drawTranslated(@Nullable BOLLImpl lastPoint, @NonNull BOLLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
        view.drawChildLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
        view.drawChildLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        BOLLImpl point = (BOLLImpl) view.getItem(position);
        text = "UP:" + view.formatValue(point.getUp()) + " ";
        canvas.drawText(text, x, y, mUpPaint);
        x += mUpPaint.measureText(text);
        text = "MB:" + view.formatValue(point.getMb()) + " ";
        canvas.drawText(text, x, y, mMbPaint);
        x += mMbPaint.measureText(text);
        text = "DN:" + view.formatValue(point.getDn()) + " ";
        canvas.drawText(text, x, y, mDnPaint);
    }

    @Override
    public float getMaxValue(BOLLImpl point) {
        if (Float.isNaN(point.getUp())) {
            return point.getMb();
        }
        return point.getUp();
    }

    @Override
    public float getMinValue(BOLLImpl point) {
        if (Float.isNaN(point.getDn())) {
            return point.getMb();
        }
        return point.getDn();
    }

    /**
     * 设置up颜色
     */
    public void setUpColor(int color) {
        mUpPaint.setColor(color);
    }

    /**
     * 设置mb颜色
     * @param color
     */
    public void setMbColor(int color) {
        mMbPaint.setColor(color);
    }

    /**
     * 设置dn颜色
     */
    public void setDnColor(int color) {
        mDnPaint.setColor(color);
    }
}
