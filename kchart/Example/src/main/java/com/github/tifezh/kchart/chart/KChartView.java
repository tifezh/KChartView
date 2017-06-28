package com.github.tifezh.kchart.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.draw.BOLLDraw;
import com.github.tifezh.kchartlib.chart.draw.KDJDraw;
import com.github.tifezh.kchartlib.chart.draw.MACDDraw;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.RSIDraw;

/**
 * k线图
 * Created by tian on 2016/5/20.
 */
public class KChartView extends BaseKChartView {

    ProgressBar mProgressBar;
    private boolean isRefreshing=false;
    private boolean isLoadMoreEnd=false;
    private boolean mLastScrollEnable;
    private boolean mLastScaleEnable;
    private KChartRefreshListener mRefreshListener;

    public KChartView(Context context) {
        this(context, null);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initData();
    }

    private void initView() {
        mProgressBar=new ProgressBar(getContext());
        LayoutParams layoutParams = new LayoutParams(dp2px(75), dp2px(75));
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(mProgressBar,layoutParams);
        mProgressBar.setVisibility(GONE);
    }

    private void initData() {
        addChildDraw("MACD", new MACDDraw(getContext()));
        addChildDraw("KDJ", new KDJDraw(getContext()));
        addChildDraw("RSI", new RSIDraw(getContext()));
        addChildDraw("BOLL", new BOLLDraw(getContext()));
        setMainDraw(new MainDraw(getContext()));
    }

    @Override
    public void onLeftSide() {
        showLoading();
    }

    @Override
    public void onRightSide() {
    }

    public void showLoading()
    {
        if(!isLoadMoreEnd &&!isRefreshing)
        {
            isRefreshing=true;
            if(mProgressBar!=null)
            {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            if(mRefreshListener!=null)
            {
                mRefreshListener.onLoadMoreBegin(this);
            }
            mLastScaleEnable =isScaleEnable();
            mLastScrollEnable=isScrollEnable();
            super.setScrollEnable(false);
            super.setScaleEnable(false);
        }
    }

    private void hideLoading(){
        if(mProgressBar!=null)
        {
            mProgressBar.setVisibility(View.GONE);
        }
        super.setScrollEnable(mLastScrollEnable);
        super.setScaleEnable(mLastScaleEnable);
    }

    /**
     * 刷新完成
     */
    public void refreshCompelete()
    {
        isRefreshing=false;
        hideLoading();
    }

    /**
     * 刷新完成，没有数据
     */
    public void refreshEnd()
    {
        isLoadMoreEnd =true;
        isRefreshing=false;
        hideLoading();
    }

    /**
     * 重置加载更多
     */
    public void resetLoadMoreEnd() {
        isLoadMoreEnd=false;
    }

    public interface KChartRefreshListener{
        /**
         * 加载更多
         * @param chart
         */
        void onLoadMoreBegin(KChartView chart);
    }

    @Override
    public void setScaleEnable(boolean scaleEnable) {
        if(isRefreshing)
        {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScaleEnable(scaleEnable);

    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        if(isRefreshing)
        {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScrollEnable(scrollEnable);
    }
}
