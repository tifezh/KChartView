package com.silladus.stock.kchart.chart;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分时数据适配器
 * Created by silladus on 2017/3/6.
 */

public class MChartAdapter extends BaseKChartAdapter {

    private List<KLineEntity> datas = new ArrayList<>();

    public MChartAdapter() {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public Date getDate(int position) {
        try {
            String s = datas.get(position).Date;
//            String[] split = s.split("/");
            Date date = new Date();
            date.setMinutes(Integer.parseInt(s));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向头部添加数据
     *
     * @param data
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
//            for (int i = 0; i < data.size(); i++) {
//                datas.set(i, data.get(i));
//            }
            datas.addAll(data);
//            int ss = datas.size();
//            for (int i = 0; i < 240 - ss; i++){
//                datas.add(new KLineEntity());
//            }
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     *
     * @param data
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
//            for (int i = 0; i < data.size(); i++) {
//                datas.set(i, data.get(i));
//            }
            datas.addAll(data);

//            int ss = datas.size();
//            for (int i = 0; i < 240 - ss; i++){
//                datas.add(new KLineEntity());
//            }
            notifyDataSetChanged();
        }
    }

}
