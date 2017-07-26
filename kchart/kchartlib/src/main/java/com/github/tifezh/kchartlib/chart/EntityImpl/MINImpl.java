package com.github.tifezh.kchartlib.chart.EntityImpl;

/**
 * 分钟线实体
 * Created by silladus on 2017/3/7.
 */

public interface MINImpl {

    /**
     * 上次收盘价
     *
     * @return
     */
    float getLastPrice();

    /**
     * 昨日收盘价
     *
     * @return
     */
    float getLastClosePrice();

    /**
     * 开盘价
     *
     * @return
     */
    float getOpenPrice();

    /**
     * 最高价
     *
     * @return
     */
    float getHighPrice();

    /**
     * 最低价
     *
     * @return
     */
    float getLowPrice();

    /**
     * 收盘价
     *
     * @return
     */
    float getClosePrice();

    /**
     * 成交量
     *
     * @return
     */
    float getVolume();

    /**
     * 成交均价
     *
     * @return
     */
    float getAVPrice();

    /**
     * 成交时间
     *
     * @return
     */
    String getTime();
}
