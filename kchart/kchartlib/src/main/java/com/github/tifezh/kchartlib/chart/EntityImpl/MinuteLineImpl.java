package com.github.tifezh.kchartlib.chart.EntityImpl;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by tifezh on 2017/7/19.
 */

public interface MinuteLineImpl {

    /**
     * @return 获取均价
     */
    float getAvgPrice();

    /**
     * @return 获取成交价
     */
    float getPrice();

    Date getDate();
}
