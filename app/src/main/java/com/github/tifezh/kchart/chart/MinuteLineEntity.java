package com.github.tifezh.kchart.chart;

import com.github.tifezh.kchartlib.chart.entity.IMinuteLine;

import java.util.Date;

/**
 * 分时图实体
 * Created by tifezh on 2017/7/20.
 */

public class MinuteLineEntity implements IMinuteLine {
    /**
     * time : 09:30
     * price : 3.53
     * avg : 3.5206
     * vol : 9251
     */

    public Date time;
    public float price;
    public float avg;
    public float volume;

    @Override
    public float getAvgPrice() {
        return avg;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public Date getDate() {
        return time;
    }

    @Override
    public float getVolume() {
        return volume;
    }


}
