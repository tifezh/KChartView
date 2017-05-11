package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.MINImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;

/**
 * 分时图的实现类
 * Created by silladus on 2017/3/7.
 */

public class MINDraw extends BaseDraw<MINImpl> {

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 构造方法
     *
     * @param context
     */
    public MINDraw(Context context) {
        super(context);
        mTextPaint.setColor(context.getResources().getColor(R.color.chart_text));
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.chart_selector_text_size));
        mSelectorPaint.setColor(context.getResources().getColor(R.color.chart_selector));
        mSelectorPaint.setAlpha(200);
        ma5Paint.setColor(context.getResources().getColor(R.color.order_tab_select));
        ma10Paint.setColor(context.getResources().getColor(R.color.price_av));
    }

    @Override
    public void drawTranslated(@Nullable MINImpl lastPoint, @NonNull MINImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        //画均价
        if (lastPoint.getAVPrice() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getAVPrice(), curX, curPoint.getAVPrice());
        }
        //画实价
        if (lastPoint.getClosePrice() != 0) {
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getClosePrice(), curX, curPoint.getClosePrice());
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
//        MINImpl point = (MINImpl) view.getItem(position);
//        String text = "MA5:" + view.formatValue(point.getClosePrice()) + " ";
//        canvas.drawText(text, x, y, ma5Paint);
//        x += ma5Paint.measureText(text);
//        text = "MA10:" + view.formatValue(point.getAVPrice()) + " ";
//        canvas.drawText(text, x, y, ma10Paint);
//        x += ma10Paint.measureText(text);
    }

    @Override
    public float getMaxValue(MINImpl point) {
        return point.getLastClosePrice() + Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

    @Override
    public float getMinValue(MINImpl point) {
        return point.getLastClosePrice() - Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

}
