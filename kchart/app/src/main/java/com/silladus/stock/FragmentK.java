package com.silladus.stock;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.github.tifezh.kchartlib.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silladus.stock.kchart.chart.KChartAdapter;
import com.silladus.stock.kchart.chart.KChartView;
import com.silladus.stock.kchart.chart.KLineEntity;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silladus on 2017/3/6.
 */

public class FragmentK extends Fragment {
    @Bind(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_kline, null);
        ButterKnife.bind(this, layout);
        initView();
        initData();
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(0);
        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice() + " chg:" + String.format(Locale.getDefault(), "%.2f", data.getRate() / data.getLastClosePrice()));
            }
        });
        mKChartView.setOverScrollRange(ViewUtil.dp2Px(getActivity(), 0));
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String fileName = "ibm.json"; //k线图的数据
                String fileName = "ibm-short.json"; //k线图的数据
                String res = "";
                try {
                    InputStream in = getResources().getAssets().open(fileName);
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final List<KLineEntity> data = new Gson().fromJson(res, new TypeToken<List<KLineEntity>>() {}.getType());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        mKChartView.startAnimation();
                    }
                });
            }
        }).start();
    }
}
