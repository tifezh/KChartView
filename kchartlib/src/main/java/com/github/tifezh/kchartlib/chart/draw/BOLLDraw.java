package com.github.tifezh.kchartlib.chart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.tifezh.kchartlib.chart.BaseChartDraw;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.entity.IBOLL;
import com.github.tifezh.kchartlib.utils.CanvasUtils;
import com.github.tifezh.kchartlib.utils.XAlign;
import com.github.tifezh.kchartlib.utils.YAlign;

/**
 * BOLL实现类
 * Created by tifezh on 2016/6/19.
 */

public class BOLLDraw extends BaseChartDraw<IBOLL> {

    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 构造方法
     *
     * @param rect       显示区域
     * @param KChartView {@link BaseKChartView}
     */
    public BOLLDraw(Rect rect, BaseKChartView KChartView) {
        super(rect, KChartView);
    }

    @Override
    public void drawValues(@NonNull Canvas canvas, int start, int stop) {
        IBOLL point = getDisplayItem();
        float x = mKChartView.getTextPaint().measureText(getValueFormatter().format(getMaxValue())+" ");
        CanvasUtils.drawTexts(canvas,x,0, XAlign.LEFT, YAlign.TOP,
                new Pair<>(mUpPaint,"UP:" + mKChartView.formatValue(point.getUp()) + " "),
                new Pair<>(mMbPaint,"MB:" + mKChartView.formatValue(point.getMb()) + " "),
                new Pair<>(mDnPaint,"DN:" + mKChartView.formatValue(point.getDn()) + " "));
    }

    @Override
    protected void foreachDrawChart(Canvas canvas, int i, IBOLL curPoint, IBOLL lastPoint) {
        drawLine(canvas, mUpPaint, i, curPoint.getUp(), lastPoint.getUp());
        drawLine(canvas, mMbPaint, i, curPoint.getMb(), lastPoint.getMb());
        drawLine(canvas, mDnPaint, i, curPoint.getDn(), lastPoint.getDn());
    }

    @Override
    public float getMaxValue(IBOLL point) {
        if (Float.isNaN(point.getUp())) {
            return point.getMb();
        }
        return point.getUp();
    }

    @Override
    public float getMinValue(IBOLL point) {
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

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width)
    {
        mUpPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize)
    {
        mUpPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
        mDnPaint.setTextSize(textSize);
    }


}
