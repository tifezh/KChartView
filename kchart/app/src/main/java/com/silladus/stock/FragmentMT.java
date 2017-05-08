package com.silladus.stock;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tifezh.kchartlib.chart.OrderView;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.chart.impl.IKChartView;
import com.silladus.stock.kchart.bean.ConstantTest;
import com.silladus.stock.kchart.bean.DataParse;
import com.silladus.stock.kchart.chart.KLineEntity;
import com.silladus.stock.kchart.chart.MChartAdapter;
import com.silladus.stock.kchart.chart.MTrendView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by silladus on 2017/3/6.
 */

public class FragmentMT extends Fragment {
    @Bind(R.id.mt_view)
    MTrendView minView;
    @Bind(R.id.mt_order_view)
    OrderView orderView;
    private MChartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mt, null);
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
        orderView.setTopPadding(MTrendView.PADDING_VALUE);
        //定义顶部栏
        orderView.setBtnListData(new String[]{"五档", "明细"});
//        orderView.setBtnListData(new String[]{"五档"});
        mAdapter = new MChartAdapter();
        minView.setAdapter(mAdapter);
        minView.setDateTimeFormatter(new TimeFormatter());
        minView.setGridRows(4);
        minView.setGridColumns(4);
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

                orderView.setClose(mData.baseValue);
                List<String[]> buys = new ArrayList<String[]>();
                List<String[]> sells = new ArrayList<String[]>();
                String[] ss = {"3.55", "1000"};
                for (int i = 0; i < 5; i++) {
                    buys.add(ss);
                    sells.add(ss);
                }
                orderView.setOrder(buys, sells);

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
