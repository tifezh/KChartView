package com.github.tifezh.kchart;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.tifezh.kchart.chart.KChartAdapter;
import com.github.tifezh.kchart.chart.KLineEntity;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tifezh on 2017/7/3.
 */

public class LoadMoreActivity extends AppCompatActivity implements KChartView.KChartRefreshListener{
    @Bind(R.id.ll_status)
    LinearLayout mLlStatus;
    @Bind(R.id.kchart_view)
    KChartView mKChartView;
    @Bind(R.id.title_view)
    RelativeLayout mTitleView;
    private KChartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_light);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        mKChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener(){
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    private void initData() {
        mKChartView.showLoading();
        mKChartView.setRefreshListener(this);
        onLoadMoreBegin(mKChartView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLlStatus.setVisibility(View.GONE);
            mKChartView.setGridRows(3);
            mKChartView.setGridColumns(8);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLlStatus.setVisibility(View.VISIBLE);
            mKChartView.setGridRows(4);
            mKChartView.setGridColumns(4);
        }
    }

    @Override
    public void onLoadMoreBegin(KChartView chart) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLineEntity> data = DataRequest.getData(LoadMoreActivity.this,mAdapter.getCount(),500);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!data.isEmpty()) {
                    Log.i("onLoadMoreBegin", "start:" + data.get(0).getDatetime() + " stop:" + data.get(data.size() - 1).getDatetime());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //第一次加载时开始动画
                        if(mAdapter.getCount()==0)
                        {
                            mKChartView.startAnimation();
                        }
                        mAdapter.addFooterData(data);
                        //加载完成，还有更多数据
                        if(data.size()>0)
                        {
                            mKChartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            mKChartView.refreshEnd();
                        }
                    }
                });
            }
        }).start();
    }
}
