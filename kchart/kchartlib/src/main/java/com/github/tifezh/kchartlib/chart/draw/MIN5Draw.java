package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.MINImpl;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.github.tifezh.kchartlib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 五日分时图的实现类
 * Created by silladus on 2017/3/7.
 */

public class MIN5Draw extends BaseDraw<MINImpl> {

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 构造方法
     *
     * @param context
     */
    public MIN5Draw(Context context) {
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

        //画实价
        if (lastPoint.getClosePrice() != 0) {
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getClosePrice(), curX, curPoint.getClosePrice());
        }
        //画均价
        if (lastPoint.getAVPrice() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getAVPrice(), curX, curPoint.getAVPrice());
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
        if (view.isLongPress()) {
            drawSelector(view, canvas);
        }
    }

    @Override
    public float getMaxValue(MINImpl point) {
        return point.getLastClosePrice() + Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

    @Override
    public float getMinValue(MINImpl point) {
        return point.getLastClosePrice() - Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

    /**
     * draw选择器
     *
     * @param view
     * @param canvas
     */
    private void drawSelector(IKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int index = view.getSelectedIndex();
        float padding = ViewUtil.dp2Px(getContext(), 5);
        float margin = ViewUtil.dp2Px(getContext(), 5);
        float width = 0;
        float left;
        float top = margin;
        float height = padding * 8 + textHeight * 5;

        MINImpl point = (MINImpl) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(view.formatDateTime(view.getAdapter().getDate(index)));
        strings.add("高:" + point.getHighPrice());
        strings.add("低:" + point.getLowPrice());
        strings.add("开:" + point.getOpenPrice());
        strings.add("收:" + point.getClosePrice());

        for (String s : strings) {
            width = Math.max(width, mTextPaint.measureText(s));
        }
        width += padding * 2;

        float x = view.translateXtoX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }

        RectF r = new RectF(left, top, left + width, top + height);
        canvas.drawRoundRect(r, padding, padding, mSelectorPaint);
        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;

        for (String s : strings) {
            canvas.drawText(s, left + padding, y, mTextPaint);
            y += textHeight + padding;
        }

    }
}
