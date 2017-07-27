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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silladus.stock.kchart.bean.MinLineEntity;
import com.silladus.stock.kchart.chart.KLineEntity;
import com.silladus.stock.kchart.chart.MChartAdapter;
import com.silladus.stock.kchart.chart.MTrend5View;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silladus on 2017/3/6.
 */

public class FragmentMT5 extends Fragment {
    @Bind(R.id.min5_view)
    MTrend5View minView;
    private MChartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mt5, null);
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
        mAdapter = new MChartAdapter();
        minView.setAdapter(mAdapter);
        minView.setDateTimeFormatter(new DateFormatter());
        minView.setGridRows(4);
        minView.setGridColumns(5);
        minView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    String fileName = "min5_sz000835_" + i + ".json"; //分时图数据
                    String res = "";
                    try {
                        InputStream in = getResources().getAssets().open(fileName);
                        int length = in.available();
                        byte[] buffer = new byte[length];
                        in.read(buffer);
                        res = EncodingUtils.getString(buffer, "UTF-8");
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final List<MinLineEntity> data = new Gson().fromJson(res, new TypeToken<List<MinLineEntity>>() {
                    }.getType());
                    setTrendMin(data);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mAdapter.addHeaderData(datas);
                        mAdapter.addFooterData(datas);
                        minView.startAnimation();
                    }
                });
            }
        }).start();
    }

    private List<KLineEntity> datas = new ArrayList<>();

    private void setTrendMin(List<MinLineEntity> data) {
        for (int i = 0; i < data.size(); i++) {
            KLineEntity min = new KLineEntity();
            min.isMinDraw = true;
            min.Close = (float) data.get(i).price;
            min.avPrice = (float) data.get(i).avg;
            min.lastPrice = (float) (i > 0 ? data.get(i - 1).price : 3.54f);
            min.lastClosePrice = 9.70f;
            min.Volume = data.get(i).vol * 100;
            min.Date = data.get(i).time;
            min.High = 10.44f;
            min.Low = 9.59f;
            datas.add(min);
        }
    }
}
