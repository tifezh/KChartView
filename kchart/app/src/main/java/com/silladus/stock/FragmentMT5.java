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
import com.silladus.stock.kchart.bean.ConstantTest;
import com.silladus.stock.kchart.bean.DataParse;
import com.silladus.stock.kchart.chart.KLineEntity;
import com.silladus.stock.kchart.chart.MChartAdapter;
import com.silladus.stock.kchart.chart.MTrend5View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private DataParse mData;

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = new DataParse();
                JSONObject object = null;
                try {
                    object = new JSONObject(ConstantTest.MINUTESURL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mData.parseMinutes(object);
                final List<KLineEntity> data = new ArrayList<KLineEntity>();
                for (int i = 0; i < mData.getDatas().size(); i++) {
                    KLineEntity min = new KLineEntity();
                    min.isMinDraw = true;
                    min.Close = mData.getDatas().get(i).cjprice;
                    min.avPrice = mData.getDatas().get(i).avprice;
                    min.lastPrice = i > 0 ? mData.getDatas().get(i - 1).cjprice : mData.baseValue;
                    min.lastClosePrice = mData.baseValue;
                    min.Volume = mData.getDatas().get(i).cjnum;
                    min.Date = mData.getDatas().get(i).time;
                    min.High = mData.baseValue + Math.abs(mData.getDatas().get(i).cha);
                    min.Low = mData.baseValue - Math.abs(mData.getDatas().get(i).cha);
                    data.add(min);
                }


                DataHelper.calculate(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mAdapter.addHeaderData(data);
                        mAdapter.addFooterData(data);
                        minView.startAnimation();
                    }
                });
            }
        }).start();
    }
}
