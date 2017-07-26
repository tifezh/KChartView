package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.impl.IValueFormatter;

import java.util.Locale;

/**
 * Value格式化类
 * Created by tifezh on 2016/6/21.
 */

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        return String.format(Locale.getDefault(), "%.2f", value);
    }

    public String format(float value, int type) {
        return type == 4 ? String.format(Locale.getDefault(), "%.4f", value) : format(value);
    }
}
