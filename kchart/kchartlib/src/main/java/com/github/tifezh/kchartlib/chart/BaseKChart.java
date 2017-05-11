package com.github.tifezh.kchartlib.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.EntityImpl.KLineImpl;
import com.github.tifezh.kchartlib.chart.draw.MIN5Draw;
import com.github.tifezh.kchartlib.chart.draw.MINDraw;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.VOLDraw;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.chart.impl.IAdapter;
import com.github.tifezh.kchartlib.chart.impl.IChartDraw;
import com.github.tifezh.kchartlib.chart.impl.IDateTimeFormatter;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.github.tifezh.kchartlib.chart.impl.IValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.tifezh.kchartlib.utils.ViewUtil.dp2Px;
import static com.github.tifezh.kchartlib.utils.ViewUtil.sp2px;

/**
 * k线图
 * Created by tian on 2016/5/3.
 */
public abstract class BaseKChart extends ScrollAndScaleView implements
        IKChartView, IKChartView.OnSelectedChangedListener {
    private boolean isShowChildTabIndicator = false;

    public void showChildTabIndicator(boolean isShowChildTabIndicator) {
        this.isShowChildTabIndicator = isShowChildTabIndicator;
    }

    protected int mChildDrawPosition = 0;
    protected float mMinCount = 242f;
    protected float m5MinCount = 1210f;
    //x轴的偏移量
    protected float mTranslateX = Float.MIN_VALUE;
    //k线图形区域的高度
    protected int mMainHeight = 0;
    //图形区域的宽度
    protected int mWidth = 0;
    //下方子图的高度
    protected int mChildHeight = 0;
    //图像上padding
    protected int mTopPadding = 15;
    //图像下padding
    protected int mBottomPadding = 15;
    //k线图和子图像之间的空隙
    protected int mMainChildSpace = 30;
    //k线图y轴的缩放
    protected float mMainScaleY = 1;
    //子图轴的缩放
    protected float mChildScaleY = 1;
    //数据的真实长度
    protected int mDataLen = 0;
    //k线图当前显示区域的最大值
    protected float mMainMaxValue = Float.MAX_VALUE;
    //k线图当前显示区域的最小值
    protected float mMainMinValue = Float.MIN_VALUE;
    //子图当前显示区域的最大值
    protected float mChildMaxValue = Float.MAX_VALUE;
    //子图当前实现区域的最小值
    protected float mChildMinValue = Float.MIN_VALUE;
    //显示区域中X开始点在数组的位置
    protected int mStartIndex = 0;
    //显示区域中X结束点在数组的位置
    protected int mStopIndex = 0;
    //一个点的宽度
    protected int mPointWidth = 6;
    //grid的行数
    protected int mGridRows = 4;
    //grid的列数
    private int mGridColumns = 4;
    //字体大小
    protected int mTextSize = 10;
    //表格画笔
    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文字画笔
    protected Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //背景画笔
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //长按之后选择的点的序号
    protected int mSelectedIndex;
    //Main区域的画图方法
    private IChartDraw mMainDraw;
    //数据适配器
    private IAdapter mAdapter;
    //数据观察者
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };

    private float lastX = 0;

    /**
     * 外部嵌套ScrollView等会产生时间冲突的解决
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isViewEvent = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - lastX) > 60) {
                    isViewEvent = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isViewEvent = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isViewEvent = true;
                break;
        }
        if (isViewEvent) {
            //禁止父容器拦截当前事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMainDraw instanceof MainDraw) {
                    if (event.getPointerCount() == 1) {
                        //长按之后移动
                        if (isLongPress) {
                            onLongPress(event);
                        }
                    }
                } else {
                    //长按之后移动
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
        }
        this.mDetector.onTouchEvent(event);
        if (mMainDraw instanceof MainDraw) {
            this.mScaleDetector.onTouchEvent(event);
        }
        return true;
    }

    //当前点的个数
    private int mItemCount;
    //每个点的x坐标
    private List<Float> mXs = new ArrayList<>();
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();

    private IValueFormatter mValueFormatter;
    private IDateTimeFormatter mDateTimeFormatter;

    protected KChartTabView mKChartTabView;

    private ValueAnimator mAnimator;

    private long mAnimationDuration = 0;

    //    private int mBackgroundColor = Color.parseColor("#202326");
    private int mBackgroundColor = getResources().getColor(R.color.white);

    private float mOverScrollRange = 0;

    private OnSelectedChangedListener mOnSelectedChangedListener = null;

    public BaseKChart(Context context) {
        super(context);
        init();
    }

    public BaseKChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = dp2Px(getContext(), mTopPadding);
        mBottomPadding = dp2Px(getContext(), mBottomPadding);
        mMainChildSpace = dp2Px(getContext(), mMainChildSpace);
        mPointWidth = dp2Px(getContext(), mPointWidth);
        mTextSize = sp2px(getContext(), mTextSize);
        mBackgroundPaint.setColor(getResources().getColor(R.color.chart_background));
        mGridPaint.setColor(getResources().getColor(R.color.chart_background));
        mGridPaint.setStrokeWidth(dp2Px(getContext(), 1));
        mTextPaint.setColor(getResources().getColor(R.color.chart_text));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(dp2Px(getContext(), 0.5f));

        mKChartTabView = new KChartTabView(getContext());
        addView(mKChartTabView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mKChartTabView.setOnTabSelectListener(new KChartTabView.TabSelectListener() {
            @Override
            public void onTabSelected(int type) {
                setChildDraw(type);
            }
        });

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        startAnimation();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMainChildSpace = mKChartTabView.getMeasuredHeight() + dp2Px(getContext(), 1);
        int displayHeight = h - mTopPadding - mBottomPadding - mMainChildSpace;
        this.mMainHeight = (int) (displayHeight * 0.75f);
        this.mChildHeight = (int) (displayHeight * 0.25f);
        this.mWidth = w;
        mKChartTabView.setTranslationY(mMainHeight + mTopPadding);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        if (mWidth == 0 || mMainHeight == 0 || mItemCount == 0) {
            return;
        }
        calculateValue();
        canvas.save();
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        drawGird(canvas);
        drawK(canvas);
        drawText(canvas);
        drawValue(canvas, isLongPress ? mSelectedIndex : mStopIndex);
        canvas.restore();
    }

    public float getMainY(float value) {
        return (mMainMaxValue - value) * mMainScaleY;
    }

    public float getChildY(float value) {
        return (mChildMaxValue - value) * mChildScaleY + mMainHeight + mMainChildSpace;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * 画表格
     *
     * @param canvas
     */
    private void drawGird(Canvas canvas) {
        //-----------------------上方k线图------------------------
        //横向的grid
        float rowSpace = mMainHeight / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i, mWidth, rowSpace * i, mGridPaint);
        }
        //-----------------------下方子图------------------------
        canvas.drawLine(0, mMainHeight + mMainChildSpace, mWidth, mMainHeight + mMainChildSpace, mGridPaint);
        canvas.drawLine(0, mMainHeight + mMainChildSpace + mChildHeight, mWidth, mMainHeight + mMainChildSpace + mChildHeight, mGridPaint);

        //纵向的grid
        float columnSpace = mWidth / mGridColumns;
        for (int i = 0; i <= mGridColumns; i++) {
            canvas.drawLine(columnSpace * i, 0, columnSpace * i, mMainHeight, mGridPaint);
            canvas.drawLine(columnSpace * i, mMainHeight + mMainChildSpace, columnSpace * i, mMainHeight + mMainChildSpace +
                    mChildHeight, mGridPaint);
        }
    }

    /**
     * 画k线图
     *
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        if (mMainDraw instanceof MainDraw) {
            canvas.translate(mTranslateX * mScaleX, 0);
            canvas.scale(mScaleX, 1);
        }
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            float currentPointX = getX(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw instanceof MINDraw) {
                currentPointX = i * mWidth / mMinCount;
                lastX = i == 0 ? 0 : (i - 1) * mWidth / mMinCount;
            } else if (mMainDraw instanceof MIN5Draw) {
                currentPointX = i * mWidth / 1200;
                lastX = i == 0 ? 0 : (i - 1) * mWidth / 1200;
            }
            if (mMainDraw != null) {
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mChildDraw != null) {
                mChildDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }
        //画选择线
        if (isLongPress) {
            KLineImpl point = (KLineImpl) getItem(mSelectedIndex);
            float x = getX(mSelectedIndex);
            if (mMainDraw instanceof MINDraw) {
                x = mSelectedIndex * mWidth / mMinCount;
            } else if (mMainDraw instanceof MIN5Draw) {
                x = mSelectedIndex * mWidth / 1200;
            }
            float y = getMainY(point.getClosePrice());
            canvas.drawLine(x, 0, x, mMainHeight, mTextPaint);
            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mTextPaint);
            canvas.drawLine(x, mMainHeight + mMainChildSpace, x, mMainHeight + mMainChildSpace + mChildHeight, mTextPaint);
        }
        //还原 平移缩放
        canvas.restore();
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;
        //--------------画上方k线图的值-------------
        if (mMainDraw != null) {
            //左边价格
            if (mMainDraw instanceof MainDraw) {
                mTextPaint.setColor(getResources().getColor(R.color.chart_text));
                canvas.drawText(formatValue(mMainMaxValue), 0, baseLine, mTextPaint);
                canvas.drawText(formatValue(mMainMinValue), 0, mMainHeight - baseLine / 4, mTextPaint);
            } else {
                mTextPaint.setColor(getResources().getColor(R.color.chart_red));
                canvas.drawText(formatValue(mMainMaxValue), 0, baseLine, mTextPaint);
                mTextPaint.setColor(getResources().getColor(R.color.chart_green));
                canvas.drawText(formatValue(mMainMinValue), 0, mMainHeight - baseLine / 4, mTextPaint);
            }
            mTextPaint.setColor(getResources().getColor(R.color.chart_text));
            float rowValue = (mMainMaxValue - mMainMinValue) / mGridRows;
            float rowSpace = mMainHeight / mGridRows;
            for (int i = 1; i < mGridRows; i++) {
                if (mMainDraw instanceof MainDraw) {
                    String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                    canvas.drawText(text, 0, fixTextY(rowSpace * i), mTextPaint);
                } else {
                    if (i % 2 == 0) {
                        String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                        canvas.drawText(text, 0, fixTextY(rowSpace * i), mTextPaint);
                    }
                }
            }
            //右边涨幅
            if (mMainDraw instanceof MainDraw) {
            } else {
                float baseValue = (mMainMaxValue + mMainMinValue) / 2f;
                String text = formatValue(100 * (mMainMaxValue - baseValue) / baseValue) + "%";
                mTextPaint.setColor(getResources().getColor(R.color.chart_red));
                canvas.drawText(text, mWidth - mTextPaint.measureText(text), baseLine, mTextPaint);
                text = "-" + text;
                mTextPaint.setColor(getResources().getColor(R.color.chart_green));
                canvas.drawText(text, mWidth - mTextPaint.measureText(text), mMainHeight - baseLine / 4, mTextPaint);
            }
            mTextPaint.setColor(getResources().getColor(R.color.chart_text));
        }
        //--------------画下方子图的值-------------
        if (mChildDraw != null) {
            canvas.drawText(textLenght(mChildMaxValue), 0, mMainHeight + mMainChildSpace + baseLine, mTextPaint);
            canvas.drawText(formatValue(mChildMinValue), 0, mMainHeight + mMainChildSpace + mChildHeight, mTextPaint);
        }
        //--------------画时间---------------------
        float columnSpace = mWidth / mGridColumns;
        float y = mMainHeight + mMainChildSpace + mChildHeight + baseLine;

        String[] date = {"9:30", "10:30", "11:30/13:00", "14:00", "15:00"};
        String text = "";
        for (int i = 1; i < mGridColumns; i++) {
            if (mMainDraw instanceof MINDraw) {
                if (i % 2 == 0) {
                    text = date[i];
                } else {
                    text = "";
                }
            } else {
                float translateX = xToTranslateX(columnSpace * i);
                if (translateX >= startX && translateX <= stopX) {
                    int index = indexOfTranslateX(translateX);
                    text = formatDateTime(mAdapter.getDate(index));
                } else {
                    text = "";
                }
            }
            canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTextPaint);
        }
        if (mMainDraw instanceof MainDraw) {
            float translateX = xToTranslateX(0);
            if (translateX >= startX && translateX <= stopX) {
                canvas.drawText(formatDateTime(getAdapter().getDate(mStartIndex)), 0, y, mTextPaint);
            }
            translateX = xToTranslateX(mWidth);
            if (translateX >= startX && translateX <= stopX) {
                text = formatDateTime(getAdapter().getDate(mStopIndex));
                canvas.drawText(text, mWidth - mTextPaint.measureText(text), y, mTextPaint);
            }
        } else {
            canvas.drawText(date[0], 0, y, mTextPaint);
            canvas.drawText(date[4], mWidth - mTextPaint.measureText(date[4]), y, mTextPaint);
        }
        if (isLongPress) {
            KLineImpl point = (KLineImpl) getItem(mSelectedIndex);
            text = formatValue(point.getClosePrice());
            float r = textHeight / 2;
            y = getMainY(point.getClosePrice());
            float x;
            if (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2) {
                x = 0;
                canvas.drawRect(x, y - r, mTextPaint.measureText(text), y + r, mBackgroundPaint);
            } else {
                x = mWidth - mTextPaint.measureText(text);
                canvas.drawRect(x, y - r, mWidth, y + r, mBackgroundPaint);
            }
            canvas.drawText(text, x, fixTextY(y), mTextPaint);

            // 画选中的时间
            if (!(mMainDraw instanceof MainDraw)) {
                text = point.getTime();
                y = mMainHeight;
                if (mMainDraw instanceof MINDraw) {
                    x = mSelectedIndex * mWidth / mMinCount - mTextPaint.measureText(text) / 2;
                } else if (mMainDraw instanceof MIN5Draw) {
                    x = mSelectedIndex * mWidth / 1200;
                }
                canvas.drawText(text, x, fixTextY(y), mTextPaint);
            }
        }
    }

    /**
     * 画值
     *
     * @param canvas
     * @param position 显示某个点的值
     */
    private void drawValue(Canvas canvas, int position) {
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = -dp2Px(getContext(), 1);
                float x = dp2Px(getContext(), 1);
                mMainDraw.drawText(canvas, this, position, x, y);
            }
            if (mChildDraw != null) {
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                float textHeight = fm.descent - fm.ascent;
                float baseLine = (textHeight - fm.bottom - fm.top) / 2;
                float y = mMainHeight + mMainChildSpace + baseLine;
                //最大刻度的显示
                float x = mTextPaint.measureText(textLenght(mChildMaxValue));
                mChildDraw.drawText(canvas, this, position, x, y);
            }
        }
    }

    private String textLenght(float value) {
        if (mChildDraw instanceof VOLDraw) {
            if (value > 1000000) {
                return formatValue(value / 1000000f) + "万手 ";
            } else {
                return formatValue(value / 100f) + "手 ";
            }
        } else {
            return formatValue(value) + " ";
        }
    }

    @Override
    public String formatValue(float value) {
        if (getValueFormatter() == null) {
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value);
    }

    /**
     * 重新计算并刷新线条
     */
    public void notifyChanged() {
        if (mItemCount != 0) {
            mXs.clear();
            if (mMainDraw instanceof MINDraw) {
                // 沪深交易4个小时，美股交易时间6.5个小时
                mMinCount = mItemCount < mMinCount + 1 ? mMinCount : mItemCount == mMinCount + 1 ? mItemCount : 13 * mMinCount / 8f;
                mDataLen = (int) ((mItemCount - 1) * mWidth / mMinCount);
            } else if (mMainDraw instanceof MIN5Draw) {
                mDataLen = (int) ((mItemCount - 1) * mWidth / m5MinCount);
            } else {
                mDataLen = (mItemCount - 1) * mPointWidth;
            }
            for (int i = 0; i < mItemCount; i++) {
                float x = 0;
                if (mMainDraw instanceof MINDraw) {
                    x = i * mWidth / mMinCount;
                } else if (mMainDraw instanceof MIN5Draw) {
                    x = i * mWidth / m5MinCount;
                } else {
                    x = i * mPointWidth;
                }
                mXs.add(x);
            }
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        } else {
            setScrollX(0);
        }
        invalidate();
    }

    private void calculateSelectedX(float x) {
        if (mMainDraw instanceof MainDraw) {
            mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        } else {
            mSelectedIndex = indexOfTranslateX(x);
        }
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mMainMaxValue = Float.MIN_VALUE;
        mMainMinValue = Float.MAX_VALUE;
        mChildMaxValue = Float.MIN_VALUE;
        mChildMinValue = Float.MAX_VALUE;
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            KLineImpl point = (KLineImpl) getItem(i);
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getMaxValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinValue(point));
            }
            if (mChildDraw != null) {
                mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
            }
        }
        if (mMainDraw instanceof MainDraw) {
            float padding = (mMainMaxValue - mMainMinValue) * 0.05f;
            mMainMaxValue += padding;
            mMainMinValue -= padding;
        }
        mMainScaleY = mMainHeight * 1f / (mMainMaxValue - mMainMinValue);
        mChildScaleY = mChildHeight * 1f / (mChildMaxValue - mChildMinValue);
        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }

    /**
     * 获取平移的最小值
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    @Override
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }

    @Override
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    @Override
    public Object getItem(int position) {
        if (mAdapter != null) {
            return mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    @Override
    public float getX(int position) {
        return mXs.get(position);
    }

    @Override
    public IAdapter getAdapter() {
        return mAdapter;
    }

    public int getMainHeight() {
        return mMainHeight;
    }

    public int getTabBarHeight() {
        return mMainChildSpace;
    }

    /**
     * 设置子图的绘制方法
     *
     * @param position
     */
    private void setChildDraw(int position) {
        this.mChildDraw = mChildDraws.get(position);
        mChildDrawPosition = position;
        invalidate();
    }

    @Override
    public void addChildDraw(String name, IChartDraw childDraw) {
        mChildDraws.add(childDraw);
        mKChartTabView.addTab(name, isShowChildTabIndicator);
    }

    /**
     * scrollX 转换为 TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }

    /**
     * 获取ValueFormatter
     *
     * @return
     */
    public IValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    /**
     * 设置ValueFormatter
     *
     * @param valueFormatter value格式化器
     */
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.mValueFormatter = valueFormatter;
    }

    /**
     * 获取DatetimeFormatter
     *
     * @return 时间格式化器
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return mDateTimeFormatter;
    }

    /**
     * 设置dateTimeFormatter
     *
     * @param dateTimeFormatter 时间格式化器
     */
    public void setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        mDateTimeFormatter = dateTimeFormatter;
    }

    /**
     * 格式化时间
     *
     * @param date
     */
    public String formatDateTime(Date date) {
        if (getDateTimeFormatter() == null) {
            setDateTimeFormatter(new TimeFormatter());
        }
        return getDateTimeFormatter().format(date);
    }

    /**
     * 获取主区域的 IChartDraw
     *
     * @return IChartDraw
     */
    public IChartDraw getMainDraw() {
        return mMainDraw;
    }

    /**
     * 设置主区域的 IChartDraw
     *
     * @param mainDraw IChartDraw
     */
    public void setMainDraw(IChartDraw mainDraw) {
        mMainDraw = mainDraw;
        if (mMainDraw instanceof MainDraw) {
            mKChartTabView.setVisibility(VISIBLE);
        } else {
            mKChartTabView.setVisibility(GONE);
        }
    }

    /**
     * 二分查找当前值的index
     *
     * @param translateX
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (end == start) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }

    @Override
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    @Override
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    @Override
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    /**
     * 设置表格行数
     *
     * @param gridRows
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
        invalidate();
    }

    /**
     * 设置表格列数
     *
     * @param gridColumns
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
        invalidate();
    }

    @Override
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    @Override
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    @Override
    public float getTopPadding() {
        return mTopPadding;
    }

    @Override
    public int getChartWidth() {
        return mWidth;
    }

    @Override
    public boolean isLongPress() {
        return isLongPress;
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    @Override
    public void onSelectedChanged(IKChartView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * 数据是否充满屏幕
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    @Override
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }
}
