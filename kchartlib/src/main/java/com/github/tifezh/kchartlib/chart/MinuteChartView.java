package com.github.tifezh.kchartlib.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.entity.IMinuteLine;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.formatter.BigValueFormatter;
import com.github.tifezh.kchartlib.utils.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 分时图
 * 简单的分时图示例 更丰富的需求可能需要在此基础上再作修改
 */
public class MinuteChartView extends View implements GestureDetector.OnGestureListener {

    private final static int ONE_MINUTE=60000;

    private int mHeight = 0;
    private int mWidth = 0;
    private int mVolumeHeight=100;
    private int mTopPadding = 15;
    private int mBottomPadding = 15;
    private int mGridRows = 6;
    private int GridColumns = 5;
    private Paint mAvgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPricePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBackgroundPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mVolumePaintRed=new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mVolumePaintGreen=new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mBackgroundColor;
    private float mValueMin;
    private float mValueMax;
    private float mVolumeMax;
    private float mValueStart;
    private float mScaleY = 1;
    private float mVolumeScaleY=1;
    private float mTextSize = 10;
    private boolean isLongPress = false;
    private int selectedIndex;
    private GestureDetectorCompat mDetector;
    private final List<IMinuteLine> mPoints = new ArrayList<>();
    private Date mFirstStartTime;
    private Date mFirstEndTime;
    private Date mSecondStartTime;
    private Date mSecondEndTime;
    private long mTotalTime;
    private float mPointWidth;

    private IValueFormatter mVolumeFormatter;

    public MinuteChartView(Context context) {
        super(context);
        init();
    }

    public MinuteChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MinuteChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDetector = new GestureDetectorCompat(getContext(), this);
        mTopPadding = dp2px(mTopPadding);
        mBottomPadding = dp2px(mBottomPadding);
        mTextSize = sp2px(mTextSize);
        mVolumeHeight=dp2px(mVolumeHeight);
        mGridPaint.setColor(Color.parseColor("#353941"));
        mGridPaint.setStrokeWidth(dp2px(1));
        mTextPaint.setColor(Color.parseColor("#B1B2B6"));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(dp2px(0.5f));
        mAvgPaint.setColor(Color.parseColor("#90A901"));
        mAvgPaint.setStrokeWidth(dp2px(0.5f));
        mAvgPaint.setTextSize(mTextSize);
        mPricePaint.setColor(Color.parseColor("#FF6600"));
        mPricePaint.setStrokeWidth(dp2px(0.5f));
        mPricePaint.setTextSize(mTextSize);
        mVolumePaintGreen.setColor(ContextCompat.getColor(getContext(), R.color.chart_green));
        mVolumePaintRed.setColor(ContextCompat.getColor(getContext(),R.color.chart_red));
        mBackgroundColor =Color.parseColor("#202326");
        mBackgroundPaint.setColor(mBackgroundColor);

        mVolumeFormatter=new BigValueFormatter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //一个点的时候滑动
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress) {
                        calculateSelectedX(event.getX());
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isLongPress = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                invalidate();
                break;
        }
        return true;
    }

    private void calculateSelectedX(float x) {
        selectedIndex = (int) (x * 1f / getX(mPoints.size() - 1) * (mPoints.size() - 1) + 0.5f);
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > mPoints.size() - 1) {
            selectedIndex = mPoints.size() - 1;
        }
    }

    /**
     * 根据索引获取x的值
     */
    private float getX(int position)
    {
        Date date=mPoints.get(position).getDate();
        if(mSecondStartTime!=null&&date.getTime()>=mSecondStartTime.getTime())
        {
            return 1f* (date.getTime()-mSecondStartTime.getTime()+60000+
                    mFirstEndTime.getTime()-mFirstStartTime.getTime()) /mTotalTime* (mWidth-mPointWidth)+mPointWidth/2f;
        }
        else {
            return 1f*(date.getTime()-mFirstStartTime.getTime())/mTotalTime* (mWidth-mPointWidth)+mPointWidth/2f;
        }
    }

    /**
     * 获取最大能有多少个点
     */
    private long getMaxPointCount(){
       return mTotalTime/ONE_MINUTE;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int height = h - mTopPadding - mBottomPadding;
        this.mHeight = height-mVolumeHeight;
        this.mWidth = w;
        notifyChanged();
    }

    /**
     * @param data 数据源
     * @param startTime       显示的开始时间
     * @param endTime         显示的结束时间
     * @param yesClosePrice 昨日开盘价
     */
    public void initData(Collection<? extends IMinuteLine> data,
                         Date startTime,
                         Date endTime,
                         float yesClosePrice) {
        initData(data,startTime,endTime,null,null,yesClosePrice);
    }

    /**
     * @param data 数据源
     * @param startTime       显示的开始时间
     * @param endTime         显示的结束时间
     * @param firstEndTime    休息开始时间 可空
     * @param secondStartTime 休息结束时间 可空
     * @param yesClosePrice 昨收价
     */
    public void initData(Collection<? extends IMinuteLine> data,
                         @NonNull Date startTime,
                         @NonNull Date endTime,
                         @Nullable Date firstEndTime,
                         @Nullable Date secondStartTime,
                         float yesClosePrice){
        this.mFirstStartTime = startTime;
        this.mSecondEndTime = endTime;
        if(mFirstStartTime.getTime()>=mSecondEndTime.getTime()) throw new IllegalStateException("开始时间不能大于结束时间");
        mTotalTime=mSecondEndTime.getTime()-mFirstStartTime.getTime();
        if(firstEndTime!=null&&secondStartTime!=null) {
            this.mFirstEndTime = firstEndTime;
            this.mSecondStartTime = secondStartTime;
            if(!(mFirstStartTime.getTime()<mFirstEndTime.getTime()&&
                    mFirstEndTime.getTime()<mSecondStartTime.getTime()&&
                    mSecondStartTime.getTime()<mSecondEndTime.getTime()))
            {
                throw new IllegalStateException("时间区间有误");
            }
            mTotalTime-=mSecondStartTime.getTime()-mFirstEndTime.getTime()-60000;
        }
        setValueStart(yesClosePrice);
        if (data != null) {
            mPoints.clear();
            this.mPoints.addAll(data);
        }
        notifyChanged();
    }

    /**
     * 当数据发生变化时调用
     */
    public void notifyChanged() {
        mValueMax = Float.MIN_VALUE;
        mValueMin = Float.MAX_VALUE;
        for (int i = 0; i < mPoints.size(); i++) {
            IMinuteLine point = mPoints.get(i);
            mValueMax=Math.max(mValueMax,point.getPrice());
            mValueMin=Math.min(mValueMin,point.getPrice());
            mVolumeMax=Math.max(mVolumeMax,point.getVolume());
        }
        //最大值和开始值的差值
        float offsetValueMax = mValueMax - mValueStart;
        float offsetValueMin = mValueStart - mValueMin;
        //以开始的点为中点值   上下间隙多出20%
        float offset = (offsetValueMax > offsetValueMin ? offsetValueMax : offsetValueMin) * 1.2f;
        //坐标轴高度以开始的点对称
        mValueMax = mValueStart + offset;
        mValueMin = mValueStart - offset;
        //y轴的缩放值
        mScaleY = mHeight / (mValueMax - mValueMin);
        //判断最大值和最小值是否一致
        if(mValueMax == mValueMin){
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mValueMax += Math.abs(mValueMax*0.05f);
            mValueMin -= Math.abs(mValueMax*0.05f);
            if (mValueMax == 0) {
                mValueMax = 1;
            }
        }

        if(mVolumeMax == 0){
            mVolumeMax=1;
        }

        mVolumeMax*=1.1f;
        //成交量的缩放值
        mVolumeScaleY = mVolumeHeight / mVolumeMax;
        mPointWidth=(float) mWidth/getMaxPointCount();
        mVolumePaintRed.setStrokeWidth(mPointWidth*0.8f);
        mVolumePaintGreen.setStrokeWidth(mPointWidth*0.8f);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        if (mWidth == 0 || mHeight == 0 || mPoints == null || mPoints.size() == 0) {
            return;
        }
        drawGird(canvas);
        if (mPoints.size() > 0) {
            IMinuteLine lastPoint = mPoints.get(0);
            float lastX=getX(0);
            for (int i = 0; i < mPoints.size(); i++) {
                IMinuteLine curPoint=mPoints.get(i);
                float curX=getX(i);
                canvas.drawLine(lastX, getY(lastPoint.getPrice()), curX, getY(curPoint.getPrice()), mPricePaint);
                canvas.drawLine(lastX, getY(lastPoint.getAvgPrice()),curX, getY(curPoint.getAvgPrice()), mAvgPaint);
                //成交量
                Paint volumePaint=((i==0&&curPoint.getPrice()<=mValueStart)||curPoint.getPrice()<=lastPoint.getPrice())?mVolumePaintGreen:mVolumePaintRed;
                canvas.drawLine(curX,getVolumeY(0),curX,getVolumeY(curPoint.getVolume()),volumePaint);
                lastPoint = curPoint;
                lastX=curX;
            }
        }
        drawText(canvas);
        //画指示线
        if (isLongPress) {
            IMinuteLine point = mPoints.get(selectedIndex);
            float x=getX(selectedIndex);
            canvas.drawLine(x, 0, x, mHeight+mVolumeHeight, mTextPaint);
            canvas.drawLine(0, getY(point.getPrice()), mWidth, getY(point.getPrice()), mTextPaint);
            //画指示线的时间
            String text = DateUtil.shortTimeFormat.format(point.getDate());
            x = x - mTextPaint.measureText(text) / 2;
            if (x < 0) {
                x = 0;
            }
            if (x > mWidth - mTextPaint.measureText(text)) {
                x = mWidth - mTextPaint.measureText(text);
            }
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float textHeight = fm.descent - fm.ascent;
            float baseLine = (textHeight - fm.bottom - fm.top) / 2;
            //下方时间
            canvas.drawRect(x,mHeight+mVolumeHeight-baseLine+textHeight,x+mTextPaint.measureText(text),mVolumeHeight+mHeight+baseLine,mBackgroundPaint);
            canvas.drawText(text, x, mHeight+mVolumeHeight+baseLine, mTextPaint);

            float r = textHeight / 2;
            float y=getY(point.getPrice());
            //左方值
            text=floatToString(point.getPrice());
            canvas.drawRect(0, y - r, mTextPaint.measureText(text), y + r, mBackgroundPaint);
            canvas.drawText(text, 0, fixTextY(y), mTextPaint);
            //右方值
            text=floatToString((point.getPrice() - mValueStart)*100f / mValueStart)+"%";
            canvas.drawRect(mWidth-mTextPaint.measureText(text), y - r,mWidth, y + r, mBackgroundPaint);
            canvas.drawText(text, mWidth-mTextPaint.measureText(text), fixTextY(y), mTextPaint);
        }
        drawValue(canvas, isLongPress ? selectedIndex : mPoints.size() - 1);
    }

    /**
     * 画值
     */
    private void drawValue(Canvas canvas, int index) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (index >= 0 && index < mPoints.size()) {
            float y = baseLine-textHeight;
            IMinuteLine point = mPoints.get(index);
            String text = "成交价:" + floatToString(point.getPrice()) + " ";
            float x = 0;
            canvas.drawText(text, x, y, mPricePaint);
            x += mPricePaint.measureText(text);
            text = "均价:" + floatToString(point.getAvgPrice()) + " ";
            canvas.drawText(text, x, y, mAvgPaint);
            //成交量
            text="VOL:"+mVolumeFormatter.format(point.getVolume());
            canvas.drawText(text,mWidth-mTextPaint.measureText(text),mHeight+baseLine,mTextPaint);
        }
    }

    /**
     * 修正y值
     */
    private float getY(float value) {
        return (mValueMax - value) * mScaleY;
    }

    private float getVolumeY(float value){
        return (mVolumeMax -value) * mVolumeScaleY + mHeight;
    }

    private void drawGird(Canvas canvas) {
        //先画出坐标轴
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        //横向的grid
        float rowSpace = mHeight / mGridRows;

        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i, mWidth, rowSpace * i, mGridPaint);
        }
        canvas.drawLine(0, rowSpace * mGridRows /2, mWidth, rowSpace * mGridRows /2, mGridPaint);

        canvas.drawLine(0, mHeight+mVolumeHeight, mWidth, mHeight+mVolumeHeight, mGridPaint);
        //纵向的grid
        float columnSpace = mWidth / GridColumns;
        for (int i = 0; i <= GridColumns; i++) {
            canvas.drawLine(columnSpace * i, 0, columnSpace * i, mHeight+mVolumeHeight, mGridPaint);
        }

    }
    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //画左边的值
        canvas.drawText(floatToString(mValueMax), 0, baseLine, mTextPaint);
        canvas.drawText(floatToString(mValueMin), 0, mHeight, mTextPaint);
        float rowValue = (mValueMax - mValueMin) / mGridRows;
        float rowSpace = mHeight / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            String text = floatToString(rowValue * (mGridRows - i) + mValueMin);
            if (i >= 1 && i < mGridRows) {
                canvas.drawText(text, 0, fixTextY(rowSpace * i), mTextPaint);
            }
        }
        String text = floatToString((mValueMax - mValueStart)*100f / mValueStart)+"%";
        canvas.drawText(text, mWidth - mTextPaint.measureText(text), baseLine, mTextPaint);
        text = floatToString((mValueMin - mValueStart)*100f / mValueStart)+"%";
        canvas.drawText(text, mWidth - mTextPaint.measureText(text), mHeight, mTextPaint);
        for (int i = 0; i <= mGridRows; i++) {
            text = floatToString((rowValue * (mGridRows - i) + mValueMin - mValueStart)*100f / mValueStart)+"%";
            if (i >= 1 && i < mGridRows) {
                canvas.drawText(text, mWidth - mTextPaint.measureText(text), fixTextY(rowSpace * i), mTextPaint);
            }
        }
        //画时间
        float y = mHeight+ mVolumeHeight +baseLine;
        canvas.drawText(DateUtil.shortTimeFormat.format(mFirstStartTime), 0, y, mTextPaint);
        canvas.drawText(DateUtil.shortTimeFormat.format(mSecondEndTime),
                mWidth - mTextPaint.measureText(DateUtil.shortTimeFormat.format(mSecondEndTime)), y, mTextPaint);
        //成交量
        canvas.drawText(mVolumeFormatter.format(mVolumeMax),0,mHeight+baseLine,mTextPaint);
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 保留2位小数
     */
    public String floatToString(float value) {
        String s = String.format("%.2f", value);
        char end = s.charAt(s.length() - 1);
        while (s.contains(".") && (end == '0' || end == '.')) {
            s = s.substring(0, s.length() - 1);
            end = s.charAt(s.length() - 1);
        }
        return s;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
        calculateSelectedX(e.getX());
        invalidate();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 设置开始的值 对称轴线
     */
    public void setValueStart(float valueStart) {
        this.mValueStart = valueStart;
    }

    /**
     * 修改某个点的值
     * @param position 索引值
     */
    public void changePoint(int position,IMinuteLine point)
    {
        mPoints.set(position,point);
        notifyChanged();
    }

    /**
     * 获取点的个数
     */
    private int getItemSize()
    {
        return mPoints.size();
    }

    /**
     * 刷新最后一个点
     */
    public void refreshLastPoint(IMinuteLine point) {
       changePoint(getItemSize()-1,point);
    }

    /**
     * 添加一个点
     */
    public void addPoint(IMinuteLine point) {
        mPoints.add(point);
        notifyChanged();
    }

    /**
     * 根据索引获取点
     */
    public IMinuteLine getItem(int position)
    {
        return mPoints.get(position);
    }

    /**
     * 设置成交量格式化器
     * @param volumeFormatter {@link IValueFormatter} 成交量格式化器
     */
    public void setVolumeFormatter(IValueFormatter volumeFormatter) {
        mVolumeFormatter = volumeFormatter;
    }
}
