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
                String fileName = "min.json"; //分时图数据
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
                final List<MinLineEntity> data = new Gson().fromJson(res, new TypeToken<List<MinLineEntity>>() {
                }.getType());
                setTrendMin(data);

                final List<String[]> buys = new ArrayList<String[]>();
                final List<String[]> sells = new ArrayList<String[]>();
                for (int i = 0; i < 5; i++) {
                    buys.add(new String[]{"10.55", "1000"});
                }
                int cachePrice = 352;
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    String[] ss = {"10.55", "1000"};
                    ss[0] = String.format("%.2f", cachePrice / 100f);
                    int oV = 1 | random.nextInt(100000);
                    if (oV >= 10000) {
                        float value1 = oV / 10000f;
                        ss[1] = String.format("%.2f", value1) + "万";
                    } else {
                        ss[1] = String.valueOf(oV);
                    }
                    if (i < 5) {
                        if (i == 4) {
                            ss[1] = "1.53万";
                        }
                        if (i == 3) {
                            ss[1] = "61.37万";
                        }
                        buys.set(4 - i, ss);
                    } else {
                        if (i == 5) {
                            ss[1] = "60";
                        }
                        sells.add(ss);
                    }
                    cachePrice++;
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
            min.lastClosePrice = 3.54f;
            min.Volume = data.get(i).vol * 100;
            min.Date = data.get(i).time;
            min.High = 3.57f;
            min.Low = 3.52f;
            datas.add(min);
        }
    }
}
