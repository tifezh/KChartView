package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.base.IValueFormatter;

/**
 *
 * Created by tifezh on 2017/12/13.
 */

public class BigValueFormatter implements IValueFormatter{
    @Override
    public String format(float value) {
        String unit="";
        //简单的对一万以上的数字处理 可以根据自己需求处理
        if(value>10000){
            unit="万";
            value/=10000;
        }
        return String.format("%.2f", value)+unit;
    }
}
