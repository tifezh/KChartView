package com.github.tifezh.kchart;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.chart.KChartAdapter;
import com.github.tifezh.kchart.chart.KLineEntity;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExampleActivity extends AppCompatActivity {


    @BindView(R.id.title_view)
    RelativeLayout mTitleView;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_percent)
    TextView mTvPercent;
    @BindView(R.id.ll_status)
    LinearLayout mLlStatus;
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            setContentView(R.layout.activity_example);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            setContentView(R.layout.activity_example_light);
        }
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
        mKChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    private void initData() {
        mKChartView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLineEntity> data = DataRequest.getALL(ExampleActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        mKChartView.startAnimation();
                        mKChartView.refreshEnd();
                    }
                });
            }
        }).start();
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
}
